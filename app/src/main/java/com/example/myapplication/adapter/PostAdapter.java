package com.example.myapplication.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.provider.CallLog;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a130.DbManager;
import com.example.a130.EditActivity;
import com.example.a130.MainActivity;
import com.example.a130.NewPost;
import com.example.a130.R;
import com.example.a130.utils.MyConstants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolderData> {
    private List<NewPost> arrayPost;
    private Context context;
    private OnItemClickCustom onItemClickCustom;
    private DbManager dbManager;

    public PostAdapter(List<NewPost> arrayPost, Context context, OnItemClickCustom onItemClickCustom)
    {
        this.arrayPost = arrayPost;
        this.context = context;
        this.onItemClickCustom = onItemClickCustom;
        this.dbManager = dbManager;
    }

    @NonNull
    @Override
    public ViewHolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ads,parent,false);
        return new ViewHolderData(view,onItemClickCustom);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderData holder, int position) {
        holder.setData(arrayPost.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayPost.size();
    }

    public class  ViewHolderData extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView tvPiceTel, tvDisc, tvTitle;
        private ImageView imAds;
        private OnItemClickCustom onItemClickCustom;
        private LinearLayout edit_layout;
        private ImageButton deleteButton, editButton;

        public ViewHolderData(@NonNull View itemView, OnItemClickCustom onItemClickCustom) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvPiceTel = itemView.findViewById(R.id.tvPriceTel);
            tvDisc = itemView.findViewById(R.id.tvDisk);
            imAds = itemView.findViewById(R.id.imAds);
            edit_layout = itemView.findViewById(R.id.edit_layout);
            deleteButton = itemView.findViewById(R.id.imDelete);
            editButton = itemView.findViewById(R.id.imEditItem);
            itemView.setOnClickListener(this);
            this.onItemClickCustom = onItemClickCustom;
        }
        public void setData(NewPost newPost)
        {
            if(newPost.getUid().equals(MainActivity.MAUTH))
            {
                edit_layout.setVisibility(View.VISIBLE);
            }
            else
            {
                edit_layout.setVisibility(View.GONE);
            }
            Picasso.get().load(newPost.getImageId()).into(imAds);
            tvTitle.setText(newPost.getTitle());
            String price_tel = "Цена :" + newPost.getPrice() + " Тел : " + newPost.getTel();
            tvPiceTel.setText(price_tel);
            String textDisc = null;
            if(newPost.getDick().length() > 50)
            {
                textDisc = newPost.getDick().substring(0,50) + "...";
            }
            else
            {
                textDisc = newPost.getDick();
            }
            tvDisc.setText(textDisc);

            deleteButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    deleteDialog(newPost,getAdapterPosition());
                }
            });
            editButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent i = new Intent(context, EditActivity.class);
                    i.putExtra(MyConstants.IMAGE_ID,newPost.getImageId());
                    i.putExtra(MyConstants.TITLE,newPost.getTitle());
                    i.putExtra(MyConstants.PRICE,newPost.getPrice());
                    i.putExtra(MyConstants.TEL,newPost.getTel());
                    i.putExtra(MyConstants.DISC,newPost.getDick());
                    i.putExtra(MyConstants.KEY,newPost.getKey());
                    i.putExtra(MyConstants.UID,newPost.getUid());
                    i.putExtra(MyConstants.TIME,newPost.getTime());
                    i.putExtra(MyConstants.CAT,newPost.getCat());
                    i.putExtra(MyConstants.EDIT_STATE, true);
                    context.startActivity(i);

                }
            });
        }

        @Override
        public void onClick(View v)
        {
            onItemClickCustom.onItemSelected(getAdapterPosition());
        }
    }
    private void deleteDialog(final NewPost newPost, int position)
    {
        AlertDialog.Builder builder =new AlertDialog.Builder(context);
        builder.setTitle(R.string.delete_title);
        builder.setMessage(R.string.delete_message);
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dbManager.deleteItem(newPost);
                arrayPost.remove(position);
                notifyItemRemoved(position);
            }
        });
        builder.show();
    }

    public interface OnItemClickCustom
    {
        void onItemSelected(int position);
    }
    public void updateAdapter(List<NewPost> ListData)
    {
        arrayPost.clear();
        arrayPost.addAll(ListData);
        notifyDataSetChanged();
    }
    public void setDbManager(DbManager dbManager)
    {
        this.dbManager = dbManager;
    }
}
