package com.example.mobilecomp4b.ui.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilecomp4b.R;
import com.example.mobilecomp4b.data.local.model.VisitRequestWithProperty;
import com.example.mobilecomp4b.databinding.ItemVisitRequestBinding;
import com.example.mobilecomp4b.utils.DateUtils;

public class VisitRequestAdapter extends ListAdapter<VisitRequestWithProperty, VisitRequestAdapter.VisitViewHolder> {

    public interface OnVisitRequestListener {
        void onViewProperty(int propertyId);
        void onCancelRequest(int requestId);
    }

    private final OnVisitRequestListener listener;

    public VisitRequestAdapter(OnVisitRequestListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<VisitRequestWithProperty> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<VisitRequestWithProperty>() {
                @Override
                public boolean areItemsTheSame(@NonNull VisitRequestWithProperty oldItem, @NonNull VisitRequestWithProperty newItem) {
                    return oldItem.request.getId() == newItem.request.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull VisitRequestWithProperty oldItem, @NonNull VisitRequestWithProperty newItem) {
                    return oldItem.request.getStatus().equals(newItem.request.getStatus()) &&
                            oldItem.request.getVisitDate().equals(newItem.request.getVisitDate()) &&
                            oldItem.request.getVisitTime().equals(newItem.request.getVisitTime());
                }
            };

    @NonNull
    @Override
    public VisitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemVisitRequestBinding binding = ItemVisitRequestBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new VisitViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull VisitViewHolder holder, int position) {
        holder.bind(getItem(position), listener);
    }

    public static class VisitViewHolder extends RecyclerView.ViewHolder {

        private final ItemVisitRequestBinding binding;

        public VisitViewHolder(@NonNull ItemVisitRequestBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(VisitRequestWithProperty item, OnVisitRequestListener listener) {
            if (item.property != null) {
                binding.tvRequestPropertyTitle.setText(item.property.getTitle());
                binding.tvRequestPropertyLocality.setText(item.property.getLocality() + ", " + item.property.getCity());

                Context context = binding.getRoot().getContext();
                int resId = getDrawableResId(context, item.property.getImageName());
                if (resId != 0) {
                    binding.ivRequestPropertyImage.setImageResource(resId);
                } else {
                    binding.ivRequestPropertyImage.setImageResource(R.drawable.img_default_property);
                }
            }

            binding.tvRequestStatus.setText(item.request.getStatus());
            if ("Cancelled".equalsIgnoreCase(item.request.getStatus())) {
                binding.tvRequestStatus.setTextColor(binding.getRoot().getContext().getColor(R.color.status_cancelled));
                binding.btnCancelRequest.setVisibility(View.GONE);
            } else {
                binding.tvRequestStatus.setTextColor(binding.getRoot().getContext().getColor(R.color.primary_forest));
                binding.btnCancelRequest.setVisibility(View.VISIBLE);
            }

            String displayDate = DateUtils.formatDateForDisplay(item.request.getVisitDate());
            String displayTime = DateUtils.formatTimeForDisplay(item.request.getVisitTime());
            binding.tvVisitDateTime.setText("Requested Visit: " + displayDate + " at " + displayTime);
            binding.tvSubmittedOn.setText("Submitted on: " + DateUtils.formatTimestamp(item.request.getCreatedAt()));

            binding.btnViewDetails.setOnClickListener(v -> {
                if (listener != null && item.property != null) {
                    listener.onViewProperty(item.property.getId());
                }
            });

            binding.btnCancelRequest.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCancelRequest(item.request.getId());
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
