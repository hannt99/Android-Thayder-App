package com.example.datingapp2;

import java.util.List;

import androidx.recyclerview.widget.DiffUtil;

public class CardStackCallback extends DiffUtil.Callback {

    private List<ItemModel> Old, New;

    public CardStackCallback(List<ItemModel> Old, List<ItemModel> New) {
        this.Old = Old;
        this.New = New;
    }

    @Override
    public int getOldListSize() {
        return Old.size();
    }

    @Override
    public int getNewListSize() {
        return New.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        //return Old.get(oldItemPosition).getImage() == New.get(newItemPosition).getImage();
        return Old.get(oldItemPosition).getImageLink() == New.get(newItemPosition).getImageLink();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return Old.get(oldItemPosition) == New.get(newItemPosition);
    }
}
