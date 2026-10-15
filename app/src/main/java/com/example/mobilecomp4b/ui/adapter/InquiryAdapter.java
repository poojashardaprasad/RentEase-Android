package com.example.mobilecomp4b.ui.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilecomp4b.R;
import com.example.mobilecomp4b.data.local.model.InquiryWithProperty;
import com.example.mobilecomp4b.databinding.ItemInquiryBinding;
import com.example.mobilecomp4b.utils.DateUtils;

public class InquiryAdapter extends ListAdapter<InquiryWithProperty, InquiryAdapter.InquiryViewHolder> {

    public interface OnInquiryClickListener {
        void onPropertyClick(int propertyId);
    }

    private final OnInquiryClickListener listener;

    public InquiryAdapter(OnInquiryClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<InquiryWithProperty> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<InquiryWithProperty>() {
                @Override
                public boolean areItemsTheSame(@NonNull InquiryWithProperty oldItem, @NonNull InquiryWithProperty newItem) {
                    return oldItem.inquiry.getId() == newItem.inquiry.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull InquiryWithProperty oldItem, @NonNull InquiryWithProperty newItem) {
                    return oldItem.inquiry.getMessage().equals(newItem.inquiry.getMessage());
                }
            };

    @NonNull
    @Override
    public InquiryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemInquiryBinding binding = ItemInquiryBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new InquiryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull InquiryViewHolder holder, int position) {
        holder.bind(getItem(position), listener);
    }

    public static class InquiryViewHolder extends RecyclerView.ViewHolder {

        private final ItemInquiryBinding binding;

        public InquiryViewHolder(@NonNull ItemInquiryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(InquiryWithProperty item, OnInquiryClickListener listener) {
            if (item.property != null) {
                binding.tvInquiryPropertyTitle.setText(item.property.getTitle());

                Context context = binding.getRoot().getContext();
                int resId = getDrawableResId(context, item.property.getImageName());
                if (resId != 0) {
                    binding.ivInquiryPropertyImage.setImageResource(resId);
                } else {
                    binding.ivInquiryPropertyImage.setImageResource(R.drawable.img_default_property);
                }
            }

            binding.tvInquiryDate.setText("Sent: " + DateUtils.formatTimestamp(item.inquiry.getCreatedAt()));
            binding.tvInquiryMessage.setText(item.inquiry.getMessage());

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null && item.property != null) {
                    listener.onPropertyClick(item.property.getId());
                }
            });
        }

        private int getDrawableResId(Context context, String name) {
            if (name == null || name.isEmpty()) return 0;
            try {
                Resources resources = context.getResources();
                return resources.getIdentifier(name, "drawable", context.getPackageName());
            } catch (Exception e) {
                return 0;
            }
        }
    }
}
