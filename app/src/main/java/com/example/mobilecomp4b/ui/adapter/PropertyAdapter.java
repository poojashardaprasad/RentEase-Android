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
import com.example.mobilecomp4b.data.local.entity.PropertyEntity;
import com.example.mobilecomp4b.databinding.ItemPropertyCardBinding;
import com.example.mobilecomp4b.utils.CurrencyFormatter;

public class PropertyAdapter extends ListAdapter<PropertyEntity, PropertyAdapter.PropertyViewHolder> {

    public interface OnPropertyClickListener {
        void onPropertyClick(PropertyEntity property);
        void onFavoriteToggle(PropertyEntity property);
    }

    private final OnPropertyClickListener listener;

    public PropertyAdapter(OnPropertyClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<PropertyEntity> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<PropertyEntity>() {
                @Override
                public boolean areItemsTheSame(@NonNull PropertyEntity oldItem, @NonNull PropertyEntity newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull PropertyEntity oldItem, @NonNull PropertyEntity newItem) {
                    return oldItem.getTitle().equals(newItem.getTitle()) &&
                            oldItem.getMonthlyRent() == newItem.getMonthlyRent() &&
                            oldItem.getLocality().equals(newItem.getLocality()) &&
                            oldItem.isAvailable() == newItem.isAvailable();
                }
            };

    @NonNull
    @Override
    public PropertyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPropertyCardBinding binding = ItemPropertyCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new PropertyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PropertyViewHolder holder, int position) {
        holder.bind(getItem(position), listener);
    }

    public static class PropertyViewHolder extends RecyclerView.ViewHolder {

        private final ItemPropertyCardBinding binding;

        public PropertyViewHolder(@NonNull ItemPropertyCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(PropertyEntity property, OnPropertyClickListener listener) {
            binding.tvPropertyTitle.setText(property.getTitle());
            binding.tvPropertyRent.setText(CurrencyFormatter.formatRent(property.getMonthlyRent()));
            binding.tvPropertyDescription.setText(property.getDescription());
            binding.tvPropertyLocality.setText(property.getLocality() + ", " + property.getCity());
            binding.tvPropertyBedrooms.setText(property.getBedrooms() + " BHK");
            binding.tvTenantBadge.setText(property.getTenantType());

            Context context = binding.getRoot().getContext();
            int imageResId = getDrawableResId(context, property.getImageName());
            if (imageResId != 0) {
                binding.ivPropertyImage.setImageResource(imageResId);
            } else {
                binding.ivPropertyImage.setImageResource(R.drawable.img_default_property);
            }

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) listener.onPropertyClick(property);
            });

            binding.btnFavorite.setOnClickListener(v -> {
                if (listener != null) listener.onFavoriteToggle(property);
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
