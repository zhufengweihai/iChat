package com.zf.ichat.moment;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.zf.ichat.R;
import com.zf.ichat.util.StringJoiner;

import java.util.ArrayList;
import java.util.List;

public class MomentListAdapter extends RecyclerView.Adapter<MomentListAdapter.ItemViewHolder> {
    private List<Moment> moments;

    public MomentListAdapter(List<Moment> moments) {
        this.moments = moments;
    }

    @Override
    @NonNull
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_comment, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        Moment moment = moments.get(position);
        String storageDir = Environment.getExternalStorageDirectory().toString() + "/Download/";
        Drawable path = BitmapDrawable.createFromPath(storageDir + moment.getAvatar());
        holder.avatarView.setBackground(path);
        holder.nameView.setText(moment.getName());
        holder.contentView.setText(moment.getContent());
        if (moment.getPhotos().size() > 0) {
            /*Drawable drawable = BitmapDrawable.createFromPath(storageDir + moment.getPhoto());
            int height = drawable.getIntrinsicHeight();
            int width = drawable.getIntrinsicWidth();
            ViewGroup.LayoutParams layoutParams = holder.photoView.getLayoutParams();
            if (height > width) {
                layoutParams.height = 500;
            } else {
                layoutParams.width = 500;
            }
            holder.photoView.setLayoutParams(layoutParams);
            holder.photoView.setImageDrawable(drawable);*/
            ArrayList<ImageInfo> imageInfo = new ArrayList<>();
            for (String photo : moment.getPhotos()) {
                ImageInfo info = new ImageInfo();
                String url = storageDir + photo;
                info.setThumbnailUrl(url);
                info.setBigImageUrl(url);
                imageInfo.add(info);
            }
            holder.photoView.setAdapter(new NineGridViewClickAdapterEx(holder.photoView.getContext(), imageInfo));
            holder.photoView.setVisibility(View.VISIBLE);
        } else {
            holder.photoView.setVisibility(View.GONE);
        }
        List<String> likes = moment.getLikes();
        if (likes.isEmpty()) {
            holder.likeView.setVisibility(View.GONE);
        } else {
            StringJoiner joiner = new StringJoiner(",");
            for (String like : likes) {
                joiner.add(like);
            }
            holder.likeView.setText(joiner.toString());
            holder.likeView.setVisibility(View.VISIBLE);
        }
        List<Comment> comments = moment.getComments();
        if (comments.isEmpty()) {
            holder.commentView.setVisibility(View.GONE);
        } else {
            SpannableStringBuilder ssb = new SpannableStringBuilder();
            for (Comment comment : comments) {
                SpannableStringBuilder temp = new SpannableStringBuilder(comment.getFrom());
                temp.setSpan(new ForegroundColorSpan(0xFF517fae), 0, comment.getFrom().length(), Spannable
                        .SPAN_EXCLUSIVE_EXCLUSIVE);
                ssb.append(temp);
                if (comment.getTo() == null) {
                    ssb.append(':').append(comment.getContent()).append("\n");
                } else {
                    ssb.append("回复");
                    SpannableStringBuilder temp1 = new SpannableStringBuilder(comment.getTo());
                    temp1.setSpan(new ForegroundColorSpan(0xFF517fae), 0, comment.getTo().length(), Spannable
                            .SPAN_EXCLUSIVE_EXCLUSIVE);
                    ssb.append(temp1);
                    ssb.append(':').append(comment.getContent()).append("\n");
                }
            }
            ssb.delete(ssb.length() - 1, ssb.length());
            holder.commentView.setText(ssb);
            holder.commentView.setVisibility(View.VISIBLE);
        }
        if (likes.isEmpty() && comments.isEmpty()) {
            holder.commentLayout.setVisibility(View.GONE);
        } else {
            holder.commentLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return moments.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        private final ImageView avatarView;
        private final TextView nameView;
        private final TextView contentView;
        private final NineGridView photoView;
        private final View commentLayout;
        private final TextView likeView;
        private final TextView commentView;

        private ItemViewHolder(View itemView) {
            super(itemView);
            avatarView = itemView.findViewById(R.id.avatarView);
            nameView = itemView.findViewById(R.id.nameView);
            contentView = itemView.findViewById(R.id.contentView);
            photoView = itemView.findViewById(R.id.photoView);
            commentLayout = itemView.findViewById(R.id.commentLayout);
            likeView = itemView.findViewById(R.id.likeView);
            commentView = itemView.findViewById(R.id.commentView);
        }
    }
}
