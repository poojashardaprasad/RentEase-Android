package com.example.mobilecomp4b.ui.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilecomp4b.R;
import com.example.mobilecomp4b.data.local.entity.PropertyEntity;
import com.example.mobilecomp4b.databinding.ItemPropertyCardBinding;
import com.example.mobilecomp4b.utils.CurrencyFormatter;
import com.example.mobilecomp4b.utils.ImageLoaderHelper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PropertyAdapter extends ListAdapter<PropertyEntity, PropertyAdapter.PropertyViewHolder> {

    public interface OnPropertyClickListener {
        void onPropertyClick(PropertyEntity property);
        void onFavoriteToggle(PropertyEntity property);
    }

    private final OnPropertyClickListener listener;
    private final Set<Integer> favoriteIds = new HashSet<>();

    public PropertyAdapter(OnPropertyClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    public void setFavoriteIds(List<Integer> ids) {
        favoriteIds.clear();
        if (ids != null) {
            favoriteIds.addAll(ids);
        }
        notifyDataSetChanged();
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
        holder.bind(getItem(position), listener, favoriteIds.contains(getItem(position).getId()));
    }

    public static class PropertyViewHolder extends RecyclerView.ViewHolder {

        private final ItemPropertyCardBinding binding;

        public PropertyViewHolder(@NonNull ItemPropertyCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(PropertyEntity property, OnPropertyClickListener listener, boolean isFavorite) {
            Context context = binding.getRoot().getContext();

            binding.tvPropertyTitle.setText(property.getTitle());
            binding.tvPropertyRent.setText(CurrencyFormatter.formatRent(property.getMonthlyRent()));
            binding.tvPropertyDescription.setText(property.getDescription());
            binding.tvPropertyLocality.setText(property.getLocality() + ", " + property.getCity());
            binding.tvPropertyBedrooms.setText(property.getBedrooms() + " BHK");
            binding.tvTenantBadge.setText(property.getTenantType());

            ImageLoaderHelper.loadPropertyImage(binding.ivPropertyImage, property.getImageName());

            // Update filled/empty heart icon based on favorite status
            if (isFavorite) {
                binding.btnFavorite.setImageResource(R.drawable.ic_favorite);
                binding.btnFavorite.setImageTintList(ColorStateList.valueOf(context.getColor(R.color.error_red)));
            } else {
                binding.btnFavorite.setImageResource(R.drawable.ic_favorite_border);
                binding.btnFavorite.setImageTintList(ColorStateList.valueOf(context.getColor(R.color.primary_forest)));
            }

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) listener.onPropertyClick(property);
            });

            binding.btnFavorite.setOnClickListener(v -> {
                if (listener != null) listener.onFavoriteToggle(property);
            });
        }
    }
}
