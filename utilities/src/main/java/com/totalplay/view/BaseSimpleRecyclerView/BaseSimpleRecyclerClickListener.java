package com.totalplay.view.BaseSimpleRecyclerView;

/**
 * Created by jorgehdezvilla on 11/03/17.
 * EMP
 */

public interface BaseSimpleRecyclerClickListener<Item> {

    void onBaseItemClick(Item item);

    void onBaseItemLongClick(Item item);
}
