package com.muctr.informationtech.UI;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.muctr.informationtech.FileExplorer.FileChooser;
import com.muctr.informationtech.R;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    protected FragmentManager fragmentManager;
    private static final int REQUEST_PATH = 1;
    private String curFileName;
    private String curFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getFragmentManager();
        if (findViewById(R.id.fragment_layout) != null) {
            if (savedInstanceState != null) {
                return;
            } else {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragment_layout, new ArticleListFragment());
                fragmentTransaction.commit();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                getFile();
                return true;
//            case R.id.action_favorite:
//                 User chose the "Favorite" action, mark the current item
//                 as a favorite...
//                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    public void getFile(){
        Intent intent = new Intent(this, FileChooser.class);
        startActivityForResult(intent, REQUEST_PATH);
    }

    // Listen for results.
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        // See which child activity is calling us back.
        if (requestCode == REQUEST_PATH){
            if (resultCode == RESULT_OK) {
                curFileName = data.getStringExtra("GetFileName");
                curFilePath = data.getStringExtra("GetPath");
                open(curFilePath, curFileName);
                Log.e("File path:", curFilePath+curFileName);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    private void open(String dir, String name) {
        File pdfFile = new File(dir, name);
        try {
            if (pdfFile.exists()) {
                Uri path = Uri.fromFile(pdfFile);
                Intent objIntent = new Intent(Intent.ACTION_VIEW);
                objIntent.setDataAndType(path, "application/pdf");
                objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(objIntent);
            } else {
                Toast.makeText(getApplicationContext(), "File NotFound", Toast.LENGTH_SHORT).show();
            }
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "No Viewer Application Found", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
