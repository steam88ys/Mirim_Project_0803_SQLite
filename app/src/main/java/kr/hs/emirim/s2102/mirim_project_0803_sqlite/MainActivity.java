package kr.hs.emirim.s2102.mirim_project_0803_sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    DBHelper dbHelper;
    EditText editName, editCount, editResultName, editResultCount;
    Button btnSelect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("가수 그룹 관리 DB");
        editName = findViewById(R.id.edit_name);
        editCount = findViewById(R.id.edit_count);
        editResultName = findViewById(R.id.edit_result_name);
        editResultCount = findViewById(R.id.edit_result_count);
        Button btnInit = findViewById(R.id.btn_init);
        Button btnInsert = findViewById(R.id.btn_insert);
        Button btnUpdate = findViewById(R.id.btn_update);
        Button btnDelete = findViewById(R.id.btn_delete);
        btnSelect = findViewById(R.id.btn_select);
        btnInit.setOnClickListener(btnListener);
        btnInsert.setOnClickListener(btnListener);
        btnUpdate.setOnClickListener(btnListener);
        btnDelete.setOnClickListener(btnListener);
        btnSelect.setOnClickListener(btnListener);
        dbHelper = new DBHelper(this);
    }
    View.OnClickListener btnListener = new View.OnClickListener() {
        SQLiteDatabase db;
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_init:
                    db = dbHelper.getWritableDatabase();
                    dbHelper.onUpgrade(db,1,2);
                    db.close();
                    break;

                case R.id.btn_insert:
                    db = dbHelper.getWritableDatabase();
                    db.execSQL("insert into idolTbl values('"+editName.getText().toString()+"',"+editCount.getText().toString()+" );");
                    db.close();
                    Toast.makeText(getApplicationContext(),"새로운 idol정보가 추가되었습니다.", Toast.LENGTH_LONG).show();
                    editName.setText("");
                    editCount.setText("");
                    btnSelect.callOnClick();
                    break;

                case R.id.btn_update:
                    db = dbHelper.getWritableDatabase();
                    db.execSQL("update idolTbl set cnt="+ editCount.getText().toString() +" where name='"+ editName.getText().toString() +"';");
                    btnSelect.callOnClick();
                    //db.close();
                    editName.setText("");
                    editCount.setText("");
                    break;

                case R.id.btn_delete:
                    db = dbHelper.getWritableDatabase();
                    db.execSQL("delete from idolTbl where name='"+ editName.getText().toString() +"';");
                    btnSelect.callOnClick();
                    //db.close();
                    editName.setText("");
                    editCount.setText("");
                    break;

                case R.id.btn_select:
                    db = dbHelper.getReadableDatabase();
                    Cursor c = db.rawQuery("select * from idolTbl;",null);

                    String strName = "아이돌명\r\n__________\r\n";
                    String strCnt = "인원수\r\n__________\r\n";

                    while (c.moveToNext()){
                        strName += c.getString(0) + "\r\n";
                        strCnt += c.getInt(1) + "\r\n";
                    }

                    editResultName.setText(strName);
                    editResultCount.setText(strCnt);

                    c.close();
                    db.close();
                    break;
            }
        }
    };

    public class DBHelper extends SQLiteOpenHelper{
        //db 생성
        public DBHelper(Context context){
            super(context, "idolDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table idolTbl(name char(30) primary key, "+" cnt intrtger);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            db.execSQL("drop table if exists idolTbl");
            onCreate(db);
        }
    }
}