package com.fan.player.ui.base.adapter;

/**
 * Project: FPlayer
 * User: fan
 * Date: 7/11/16
 * Time: 11:43 AM
 * Desc: Reusable list item view.
 * - http://blog.csdn.net/kroclin/article/details/41830315
 */
public interface IAdapterView<T> {

    void bind(T item, int position);
}
