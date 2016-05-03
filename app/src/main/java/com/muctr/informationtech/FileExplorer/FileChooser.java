package com.muctr.informationtech.FileExplorer;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.muctr.informationtech.R;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.util.Collections.sort;

public class FileChooser extends ListActivity {

    private File currentDir;
    private FileArrayAdapter adapter;
    private Drawable drawableFile;
    private Drawable drawableFolder;
    private Drawable drawableUpFolder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentDir = new File(Environment.getExternalStorageDirectory().getPath());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawableFile = getDrawable(R.drawable.ic_file_24dp);
            drawableFolder = getDrawable(R.drawable.ic_folder_24dp);
            drawableUpFolder = getDrawable(R.drawable.ic_up_folder_24dp);
        } else {
            drawableFile = getResources().getDrawable(R.drawable.ic_file_24dp);
            drawableFolder = getResources().getDrawable(R.drawable.ic_folder_24dp);
            drawableUpFolder = getResources().getDrawable(R.drawable.ic_up_folder_24dp);
        }
        fill(currentDir);
    }

    private void fill(File f) {

        File[] dirs = f.listFiles();

        this.setTitle("Current Dir: " + f.getName());

        List<ItemFile> directories = new ArrayList<>();
        List<ItemFile> files = new ArrayList<>();

        try {
            for (File file : dirs) {
                Date lastModDate = new Date(file.lastModified());
                DateFormat formater = DateFormat.getDateTimeInstance();
                String date_modify = formater.format(lastModDate);

                if (file.isDirectory()) {
                    String filesCount = getNumberOfFiles(file.listFiles());

                    if (!isHiddenFile(file.getName())) {
                        directories.add(new ItemFile(file.getName(), filesCount, date_modify, file.getAbsolutePath(), drawableFolder, true));
                    }
                } else {
                    if (!isHiddenFile(file.getName())) {
                        files.add(new ItemFile(file.getName(), getFilesSize(file.length()), date_modify, file.getAbsolutePath(), drawableFile));
                    }
                }
            }
        } finally {
            sort(directories);
            sort(files);
            directories.addAll(files);
            if (!f.getName().equalsIgnoreCase("sdcard")) {
                directories.add(0, new ItemFile("...", "Parent Directory", "", f.getParent(), drawableUpFolder, true));
            }
            adapter = new FileArrayAdapter(FileChooser.this, R.layout.file_explorer_row, directories);
            this.setListAdapter(adapter);
        }
    }

    private boolean isHiddenFile(@NonNull String name) {
        return name.substring(0, 1).equals(".");
    }

    private String getNumberOfFiles(File[] files) {
        int buf;
        if (files != null) {
            buf = files.length;
        } else {
            buf = 0;
        }
        String num_item = String.valueOf(buf);

        if (buf == 0) {
            return num_item + " item";
        } else {
            return num_item + " items";
        }
    }

    private String getFilesSize(long size) {
        if (size < 1024) {
            return size + " byte";
        } else {
            size = size / 1024;
            if (size < 1024) {
                return size + " KB";
            } else {
                size = size / 1024;
                return size + " MB";
            }
        }
    }

    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        try {
            ItemFile itemFile = adapter.getItem(position);
            if (itemFile.isDirectory()) {
                currentDir = new File(itemFile.getPath());
                fill(currentDir);
            } else {
                onFileClick(itemFile);
            }
        } catch (Exception ex) {
            Log.e("FileExplorer", ex.getMessage());
        }
    }

    //    TODO: check on pdf format file
    private void onFileClick(ItemFile itemFile) {
        Intent intent = new Intent();
        intent.putExtra("GetPath", currentDir.toString());
        intent.putExtra("GetFileName", itemFile.getName());
        setResult(RESULT_OK, intent);
        finish();
    }

}
