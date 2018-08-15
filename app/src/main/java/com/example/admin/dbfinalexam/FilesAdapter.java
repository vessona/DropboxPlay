package com.example.admin.dbfinalexam;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.FolderMetadata;
import com.dropbox.core.v2.files.Metadata;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Adapter for file list
 */
public class FilesAdapter extends RecyclerView.Adapter<FilesAdapter.MetadataViewHolder> {
    private List<Metadata> mFiles;
    private final Picasso mPicasso;
    private final Callback mCallback;



    private boolean multiSelect = false;
    private ArrayList<Metadata> selectedItems = new ArrayList<Metadata>();
    private ActionMode.Callback actionModeCallbacks = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            multiSelect = true;
            menu.add("Delete");
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (item.getTitle().equals("Delete") )
            {
                for (Metadata intItem : selectedItems) {
                    mCallback.onFileLongClick((FileMetadata)intItem);
                    mFiles.remove(intItem);

                }
            }


            mode.finish();
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            multiSelect = false;
            selectedItems.clear();
            notifyDataSetChanged();
            mCallback.RestoreToolbar();
        }

    };





    public void setFiles(List<Metadata> files) {
        //mFiles = Collections.unmodifiableList(new ArrayList<>(files));
        mFiles = new ArrayList<>(files);
        notifyDataSetChanged();
    }

    public interface Callback {
        void onFolderClicked(FolderMetadata folder);
        void onFileClicked(FileMetadata file);
        void onFileLongClick(FileMetadata file);
        void RestoreToolbar();
    }

    public FilesAdapter(Picasso picasso, Callback callback) {
        mPicasso = picasso;
        mCallback = callback;
    }

    @Override
    public MetadataViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.files_item, viewGroup, false);
        return new MetadataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MetadataViewHolder metadataViewHolder, int i) {
        metadataViewHolder.update(mFiles.get(i));
    }

    @Override
    public long getItemId(int position) {
        return mFiles.get(position).getPathLower().hashCode();
    }

    @Override
    public int getItemCount() {
        return mFiles == null ? 0 : mFiles.size();
    }

    public class MetadataViewHolder extends RecyclerView.ViewHolder  {
        private final TextView mTextView;
        private final ImageView mImageView;
        private Metadata mItem;
        CardView cardView;

        public MetadataViewHolder(final View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.item_card);
            mTextView = (TextView)itemView.findViewById(R.id.text);
            mImageView = (ImageView) itemView.findViewById(R.id.img);

        }






        void selectItem(Metadata item) {
            if (multiSelect) {
                if (selectedItems.contains(item)) {
                    selectedItems.remove(item);
                    cardView.setBackgroundColor(Color.WHITE);
                } else {
                    selectedItems.add(item);
                    cardView.setBackgroundColor(Color.LTGRAY);
                }

            }
            else
            {
                if (mItem instanceof FolderMetadata) {
                    mCallback.onFolderClicked((FolderMetadata) mItem);
                }  else if (mItem instanceof FileMetadata) {
                    mCallback.onFileClicked((FileMetadata)mItem);
                }
            }
        }


        void update(final Metadata value) {
            mItem = value;
            mTextView.setText(value.getName());

            if (value instanceof FileMetadata) {
                MimeTypeMap mime = MimeTypeMap.getSingleton();
                String ext = value.getName().substring(value.getName().indexOf(".") + 1);
                String type = mime.getMimeTypeFromExtension(ext);
                if (type != null && type.startsWith("image/")) {
                    mPicasso.load(FileThumbnailRequestHandler.buildPicassoUri((FileMetadata)value))
                            .placeholder(R.drawable.image)
                            .error(R.drawable.image)
                            .into(mImageView);
                } else {
                    mPicasso.load(R.drawable.file)
                            .noFade()
                            .into(mImageView);
                }
            } else if (value instanceof FolderMetadata) {
                mPicasso.load(R.drawable.folder)
                        .noFade()
                        .into(mImageView);
            }
            if (selectedItems.contains(value)) {
                cardView.setBackgroundColor(Color.LTGRAY);
            } else {
                cardView.setBackgroundColor(Color.WHITE);
            }
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    ((AppCompatActivity)view.getContext()).startSupportActionMode(actionModeCallbacks);
                    ((AppCompatActivity) view.getContext()).getSupportActionBar().hide();
                    selectItem(value);
                    return true;
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectItem(value);

                }
            });
        }


    }
}