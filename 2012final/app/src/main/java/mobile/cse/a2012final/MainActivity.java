package mobile.cse.a2012final;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Presentation;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {

    final static int REQUEST_SUB = 0;

    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;
    int memoNum = 0;
    int selected;

    ProgDialogFragment progressDialog;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arrayList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);

        ListView lvMemo = findViewById(R.id.lvMemo);
        lvMemo.setAdapter(arrayAdapter);

        registerForContextMenu(lvMemo);

        lvMemo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected = position;
                Intent intent = new Intent(MainActivity.this, SubActivity.class);
                intent.putExtra("Text", arrayList.get(selected));
                startActivityForResult(intent, REQUEST_SUB);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SUB && resultCode == RESULT_OK) {
            arrayList.set(selected, data.getStringExtra("Changed"));
            arrayAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, "New Text");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                arrayList.add("NewMemo " + memoNum++);
                arrayAdapter.notifyDataSetChanged();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.cm_delete:
                arrayList.remove(info.position);
                arrayAdapter.notifyDataSetChanged();
                break;
            case R.id.cm_upload:
                progressDialog = new ProgDialogFragment();
                new UploadTask(progressDialog,100).execute(10);
                break;
        }
        return super.onContextItemSelected(item);
    }

    public class UploadTask extends AsyncTask<Integer,Integer,Void>{

        ProgDialogFragment dialog;
        int size;

        UploadTask(ProgDialogFragment dialog, int size){
            this.dialog = dialog;
            this.size = size;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog.setCancelable(false);
            dialog.show(getSupportFragmentManager(),"Upload");
            getSupportFragmentManager().executePendingTransactions();

            progressBar = dialog.getDialog().findViewById(R.id.progressBar);
            progressBar.setMax(size);
            progressBar.setProgress(0);

        }

        @Override
        protected Void doInBackground(Integer... integers) {
            int speed = integers[0];
            int remain = size;

            while (remain>0){
                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                publishProgress(speed);
                remain-=speed;
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.incrementProgressBy(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }

    public static class ProgDialogFragment extends DialogFragment{
        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Upload")
                    .setView(View.inflate(getActivity(),R.layout.dialog_progress,null));
            return builder.create();
        }
    }

}