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
import com.example.mobilecomp4b.utils.UserSessionManager;

public class VisitRequestAdapter extends ListAdapter<VisitRequestWithProperty, VisitRequestAdapter.VisitViewHolder> {

    public interface OnVisitRequestListener {
        void onViewProperty(int propertyId);
        void onCancelRequest(int requestId);
        void onAcceptRequest(int requestId);
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
            Context context = binding.getRoot().getContext();
            UserSessionManager sessionManager = new UserSessionManager(context);

            if (item.property != null) {
                binding.tvRequestPropertyTitle.setText(item.property.getTitle());
                binding.tvRequestPropertyLocality.setText(item.property.getLocality() + ", " + item.property.getCity());

                com.example.mobilecomp4b.utils.ImageLoaderHelper.loadPropertyImage(binding.ivRequestPropertyImage, item.property.getImageName());
            }

            String status = item.request.getStatus();
            if ("Accepted".equalsIgnoreCase(status)) {
                binding.tvRequestStatus.setText("Accepted ✓");
                binding.tvRequestStatus.setTextColor(context.getColor(R.color.primary_forest));
                binding.btnAcceptRequest.setVisibility(View.GONE);
                binding.btnCancelRequest.setVisibility(View.GONE);
            } else if ("Cancelled".equalsIgnoreCase(status)) {
                binding.tvRequestStatus.setText("Cancelled");
                binding.tvRequestStatus.setTextColor(context.getColor(R.color.status_cancelled));
                binding.btnAcceptRequest.setVisibility(View.GONE);
                binding.btnCancelRequest.setVisibility(View.GONE);
            } else { // Pending
                binding.tvRequestStatus.setText("Pending");
                binding.tvRequestStatus.setTextColor(context.getColor(R.color.status_pending));
                binding.btnCancelRequest.setVisibility(View.VISIBLE);
                if (sessionManager.isOwner()) {
                    binding.btnCancelRequest.setText("Decline");
                    binding.btnAcceptRequest.setVisibility(View.VISIBLE);
                    binding.btnAcceptRequest.setText("Accept");
                } else {
                    binding.btnCancelRequest.setText("Cancel");
                    binding.btnAcceptRequest.setVisibility(View.GONE);
                }
            }

            String displayDate = DateUtils.formatDateForDisplay(item.request.getVisitDate());
            String displayTime = DateUtils.formatTimeForDisplay(item.request.getVisitTime());
            binding.tvVisitDateTime.setText("Requested Visit: " + displayDate + " at " + displayTime);
            binding.tvSubmittedOn.setText("Submitted by: " + item.request.getName() + " (" + item.request.getPhone() + ")");

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

            binding.btnAcceptRequest.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onAcceptRequest(item.request.getId());
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
