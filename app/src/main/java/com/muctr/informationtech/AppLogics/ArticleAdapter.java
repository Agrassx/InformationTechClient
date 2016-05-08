package com.muctr.informationtech.AppLogics;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.muctr.informationtech.AppLogics.Article;
import com.muctr.informationtech.DataBase.DataBaseHandler;
import com.muctr.informationtech.R;
import com.muctr.informationtech.UI.ArticleListFragment;

import java.util.List;

public class ArticleAdapter extends ArrayAdapter<Article>  {
    private final List<Article> values;

    public ArticleAdapter(Context context, List<Article> values) {
        super(context, R.layout.article_list_item, values);
        this.values = values;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.article_list_item, parent, false);
        }

        final DataBaseHandler dataBase = ArticleListFragment.dataBase;


        TextView name = (TextView) convertView.findViewById(R.id.textViewName);
        CheckBox favoriteCheckBox = (CheckBox) convertView.findViewById(R.id.checkBoxFavorite);
        favoriteCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                values.get(position).setFavorite(isChecked);
                dataBase.updateArticle(values.get(position));
            }
        });

        favoriteCheckBox.setChecked(values.get(position).isFavorite());
        name.setText(values.get(position).getName());
        return convertView;
    }
}
