package com.example.aditya.pdf_report;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Set;
import com.example.aditya.pdf_report.BuildConfig;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    PdfStamper stamper;
    PdfReader reader ;
    String[] ratings = { "1", "2","3", "4", "5" };
    EditText dob,branchcode,branchmanager,branchname,clusterhead,staffaction,staffaccount,sifaarishaction,sifaarishaccount;
    EditText staraction,staraccount,lateaction,lateaccount,bhiaction,bhiaccount,superaction,superaccount;
    EditText newaction,newaccount,attritionaction,attritionaccount,slfaction,slfaccount,mandaccount,mandaction,vbmsaction,vbmsaccount,trainingaction,trainingaccount;
    EditText pacaction,pacaccount,roleaction,roleaccount, reportname,employeefeed,discipline,other;
    AutoCompleteTextView staffrate,sifaarishrate,starrate,laterate,bhirate,superrate,newrate,attritionrate,slfrate,mandarate,vbmsrate,trainingrate;
    AutoCompleteTextView pacrate,rolerate;
    ArrayList<AutoCompleteTextView> arrayList = new ArrayList<AutoCompleteTextView>();
    ArrayList<String> fieldnames = new ArrayList<String>();
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    Calendar myCalendar = Calendar.getInstance();
    AcroFields acroFields;
    Button submit,email;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermission();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_singlechoice, ratings);
        initialiseviews();
        //Set the number of characters the user must type before the drop down list is shown
        for (int i=0;i<arrayList.size();i++) {
            arrayList.get(i).setThreshold(1);
            //Set the adapter
            arrayList.get(i).setAdapter(adapter);

        }

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        dob.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(MainActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        submit = (Button)findViewById(R.id.submit);
        email = (Button)findViewById(R.id.email);

        submit.setOnClickListener(this);
        email.setOnClickListener(this);




    }


    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat
                    .requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    // Permission Denied
                    Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT)
                            .show();
                    finish();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void initialiseviews()
    {
        staffrate = (AutoCompleteTextView)findViewById(R.id.staff_rating);
        arrayList.add(staffrate);
        sifaarishrate = (AutoCompleteTextView)findViewById(R.id.sifarish_rating);
        arrayList.add(sifaarishrate);
        starrate = (AutoCompleteTextView)findViewById(R.id.star_rating);
        arrayList.add(starrate);
        laterate = (AutoCompleteTextView)findViewById(R.id.Late_rating);
        arrayList.add(laterate);
        bhirate = (AutoCompleteTextView)findViewById(R.id.bhi_rating);
        arrayList.add(bhirate);
        superrate = (AutoCompleteTextView)findViewById(R.id.supervisory_rating);
        arrayList.add(superrate);
        newrate = (AutoCompleteTextView)findViewById(R.id.new_joinee_rating);
        arrayList.add(newrate);
        attritionrate = (AutoCompleteTextView)findViewById(R.id.attrition_rating);
        arrayList.add(attritionrate);
        slfrate = (AutoCompleteTextView)findViewById(R.id.slf_rating);
        arrayList.add(slfrate);
        mandarate = (AutoCompleteTextView)findViewById(R.id.mand_leave_rating);
        arrayList.add(mandarate);
        vbmsrate = (AutoCompleteTextView)findViewById(R.id.vbms_rating);
        arrayList.add(vbmsrate);
        trainingrate = (AutoCompleteTextView)findViewById(R.id.training_rating);
        arrayList.add(trainingrate);
        pacrate = (AutoCompleteTextView)findViewById(R.id.pac_rating);
        arrayList.add(pacrate);
        rolerate = (AutoCompleteTextView)findViewById(R.id.role_elevation_rating);
        arrayList.add(rolerate);

        staffaction = (EditText)findViewById(R.id.staff_action_plan);
        sifaarishaction = (EditText)findViewById(R.id.sifarish_action_plan);
        staraction = (EditText)findViewById(R.id.star_action_plan);
        lateaction = (EditText)findViewById(R.id.Late_action_plan);
        bhiaction = (EditText)findViewById(R.id.bhi_action_plan);
        superaction = (EditText)findViewById(R.id.supervisory_action_plan);
        newaction = (EditText)findViewById(R.id.new_joinee_action_plan);
        attritionaction = (EditText)findViewById(R.id.attrition_action_plan);
        slfaction = (EditText)findViewById(R.id.slf_action_plan);
        mandaction = (EditText)findViewById(R.id.mand_leave_action_plan);
        vbmsaction = (EditText)findViewById(R.id.vbms_action_plan);
        trainingaction = (EditText)findViewById(R.id.training_action_plan);
        pacaction = (EditText)findViewById(R.id.pac_action_plan);
        roleaction = (EditText)findViewById(R.id.role_elevation_action_plan);


        staffaccount = (EditText)findViewById(R.id.staff_accountability);
        sifaarishaccount = (EditText)findViewById(R.id.sifarish_accountability);
        staraccount = (EditText)findViewById(R.id.star_accountability);
        lateaccount = (EditText)findViewById(R.id.Late_accountability);
        bhiaccount = (EditText)findViewById(R.id.bhi_accountability);
        superaccount = (EditText)findViewById(R.id.supervisory_accountability);
        newaccount = (EditText)findViewById(R.id.new_joinee_accountability);
        attritionaccount = (EditText)findViewById(R.id.attrition_accountability);
        slfaccount = (EditText)findViewById(R.id.slf_accountability);
        mandaccount = (EditText)findViewById(R.id.mand_leave_accountability);
        vbmsaccount = (EditText)findViewById(R.id.vbms_accountability);
        trainingaccount = (EditText)findViewById(R.id.training_accountability);
        pacaccount = (EditText)findViewById(R.id.pac_accountability);
        roleaccount = (EditText)findViewById(R.id.role_elevation_accountability);


        reportname = (EditText)findViewById(R.id.title);

        dob = (EditText)findViewById(R.id.date);
        branchcode = (EditText)findViewById(R.id.branch_code);
        branchname = (EditText)findViewById(R.id.branch_name);
        branchmanager = (EditText)findViewById(R.id.branch_manager);
        clusterhead = (EditText)findViewById(R.id.cluster_head);
        employeefeed = (EditText)findViewById(R.id.employee_feedback);
        discipline = (EditText)findViewById(R.id.discipline);
        other = (EditText)findViewById(R.id.other);


    }

    public void writetopdf()
    {
        File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/pdf_report");
        if(f.isDirectory())
            Log.d("Main", "writetopdf: " + f.isDirectory());
        else {
            f.mkdir();
            Log.d("Main", "writetopdf: " + f.isDirectory());
        }

        File pdf = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/pdf_report/" + reportname.getText().toString().trim() + ".pdf");
        OutputStream output = null;
        try {
            output = new FileOutputStream(pdf);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        try {
            reader = new PdfReader( getResources().openRawResource(R.raw.templ) );
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            stamper = new PdfStamper(reader, output);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        acroFields = stamper.getAcroFields();

        Set<String> fldNames = acroFields.getFields().keySet();

        for (String fldName : fldNames) {
            Log.d("Main","some = " + fldName );
            fieldnames.add(fldName);
        }

        try {
            setthedata();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }


        stamper.setFormFlattening(true);
        try {
            stamper.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);

        dob.setText(sdf.format(myCalendar.getTime()));
    }


    public void setthedata() throws IOException, DocumentException {
        acroFields.setField(fieldnames.get(0),dob.getText().toString());
        acroFields.setField(fieldnames.get(1),branchcode.getText().toString());
        acroFields.setField(fieldnames.get(2),branchname.getText().toString());
        acroFields.setField(fieldnames.get(3),branchmanager.getText().toString());
        acroFields.setField(fieldnames.get(4),clusterhead.getText().toString());
        acroFields.setField(fieldnames.get(5),staffrate.getText().toString());
        acroFields.setField(fieldnames.get(6),staffaction.getText().toString());
        acroFields.setField(fieldnames.get(7),staffaccount.getText().toString());
        acroFields.setField(fieldnames.get(8),sifaarishrate.getText().toString());
        acroFields.setField(fieldnames.get(9),sifaarishaction.getText().toString());
        acroFields.setField(fieldnames.get(10),sifaarishaccount.getText().toString());
        acroFields.setField(fieldnames.get(11),starrate.getText().toString());
        acroFields.setField(fieldnames.get(12),staraction.getText().toString());
        acroFields.setField(fieldnames.get(13),staraccount.getText().toString());
        acroFields.setField(fieldnames.get(14),laterate.getText().toString());
        acroFields.setField(fieldnames.get(15),lateaction.getText().toString());
        acroFields.setField(fieldnames.get(16),lateaccount.getText().toString());
        acroFields.setField(fieldnames.get(17),bhirate.getText().toString());
        acroFields.setField(fieldnames.get(18),bhiaction.getText().toString());
        acroFields.setField(fieldnames.get(19),bhiaccount.getText().toString());
        acroFields.setField(fieldnames.get(20),superrate.getText().toString());
        acroFields.setField(fieldnames.get(21),superaction.getText().toString());
        acroFields.setField(fieldnames.get(22),superaccount.getText().toString());
        acroFields.setField(fieldnames.get(23),newrate.getText().toString());
        acroFields.setField(fieldnames.get(24),newaction.getText().toString());
        acroFields.setField(fieldnames.get(25),newaccount.getText().toString());
        acroFields.setField(fieldnames.get(26),attritionrate.getText().toString());
        acroFields.setField(fieldnames.get(27),attritionaction.getText().toString());
        acroFields.setField(fieldnames.get(28),attritionaccount.getText().toString());
        acroFields.setField(fieldnames.get(29),slfrate.getText().toString());
        acroFields.setField(fieldnames.get(30),slfaction.getText().toString());
        acroFields.setField(fieldnames.get(31),slfaccount.getText().toString());
        acroFields.setField(fieldnames.get(32),mandarate.getText().toString());
        acroFields.setField(fieldnames.get(33),mandaction.getText().toString());
        acroFields.setField(fieldnames.get(34),mandaccount.getText().toString());
        acroFields.setField(fieldnames.get(35),vbmsrate.getText().toString());
        acroFields.setField(fieldnames.get(36),vbmsaction.getText().toString());
        acroFields.setField(fieldnames.get(37),vbmsaccount.getText().toString());
        acroFields.setField(fieldnames.get(38),trainingrate.getText().toString());
        acroFields.setField(fieldnames.get(39),trainingaction.getText().toString());
        acroFields.setField(fieldnames.get(40),trainingaccount.getText().toString());
        acroFields.setField(fieldnames.get(41),pacrate.getText().toString());
        acroFields.setField(fieldnames.get(42),pacaction.getText().toString());
        acroFields.setField(fieldnames.get(43),pacaccount.getText().toString());
        acroFields.setField(fieldnames.get(44),rolerate.getText().toString());
        acroFields.setField(fieldnames.get(45),roleaction.getText().toString());
        acroFields.setField(fieldnames.get(46),roleaccount.getText().toString());
        acroFields.setField(fieldnames.get(47),employeefeed.getText().toString());
        acroFields.setField(fieldnames.get(48),discipline.getText().toString());
        acroFields.setField(fieldnames.get(49),other.getText().toString());
    }


    public void checkfill()
    {
        if(reportname.getText().toString().trim()=="")
            reportname.setError("Please input report name");
        else
            writetopdf();
    }


    public void sendemail()
    {
        File filelocation = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/pdf_report/" + reportname.getText().toString().trim() + ".pdf" );
        Uri path;
        path = FileProvider.getUriForFile(MainActivity.this, getPackageName(),filelocation);
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
// set the type to 'email'
        emailIntent .setType("vnd.android.cursor.dir/email");

        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        emailIntent .putExtra(Intent.EXTRA_STREAM, path);
// the mail subject
        emailIntent .putExtra(Intent.EXTRA_SUBJECT, "Here is the Branch Visit Report - Human Resources");
        startActivity(Intent.createChooser(emailIntent , "Send email..."));
    }



    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.submit:
                checkfill();
                break;

            case R.id.email:
                sendemail();
                break;

        }

    }
}
