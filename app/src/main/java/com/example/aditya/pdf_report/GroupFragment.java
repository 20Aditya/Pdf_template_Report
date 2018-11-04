package com.example.aditya.pdf_report;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.weiwangcn.betterspinner.library.BetterSpinner;

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

import at.markushi.ui.CircleButton;


public class GroupFragment extends Fragment implements View.OnClickListener {


    View view;
    PdfStamper stamper;
    int flag = 0;
    PdfReader reader ;
    static EditText title,date,time,venue,by,minutes;
    static BetterSpinner event_category;
    String[] events = { "NJ Meet", "Coffee with Top Kats", "TP Meet",  "Open Forum", "Town Hall", "Mentoship Meet", "Focused Group Discussion" };

    Calendar myCalendar = Calendar.getInstance();
    AcroFields acroFields;
    CircleButton submit,email;
    ArrayList<String> fieldnames = new ArrayList<String>();



    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public GroupFragment() {
        // Required empty public constructor
    }

    public static GroupFragment newInstance(String param1, String param2) {
        GroupFragment fragment = new GroupFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_group, container, false);

        initialiseviews(view);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),android.R.layout.simple_dropdown_item_1line, events);
        event_category.setAdapter(adapter);





        //Set Date

        date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getFragmentManager(), "DatePicker");
            }
        });





        //Set time
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment  = new TimePickerFragment();
                //newFragment.show(getActivity().getFragmentManager(), DIALOG_TIME);
                newFragment.show(getActivity().getSupportFragmentManager(),"Time");
                // if you are using the nested fragment then user the
                //newFragment.show(getChildFragmentManager(), DIALOG_TIME);
            }
        });



        //Buttons
        submit = (CircleButton)view.findViewById(R.id.submit);

        email = (CircleButton)view.findViewById(R.id.email);
        submit.setOnClickListener(this);
        email.setOnClickListener(this);


        return view;
    }


    public void initialiseviews(View view)
    {

        title = (EditText)view.findViewById(R.id.title);
        event_category = (BetterSpinner)view.findViewById(R.id.event_category);
        date = (EditText)view.findViewById(R.id.date);
        time = (EditText)view.findViewById(R.id.time);
        venue = (EditText)view.findViewById(R.id.venue);
        by = (EditText)view.findViewById(R.id.by);
        minutes = (EditText)view.findViewById(R.id.minutes);
    }





    public void writetopdf()
    {

        flag=1;
        File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/pdf_report");
        if(f.isDirectory())
            Log.d("Main", "writetopdf: " + f.isDirectory());
        else {
            f.mkdir();
            Log.d("Main", "writetopdf: " + f.isDirectory());
        }

        File pdf = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/pdf_report/" + title.getText().toString().trim() + ".pdf");
        OutputStream output = null;
        try {
            output = new FileOutputStream(pdf);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        try {
            reader = new PdfReader( getResources().openRawResource(R.raw.hrgroup) );
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


        Toast.makeText(getActivity().getApplicationContext(),"PDF Generated", Toast.LENGTH_LONG).show();


    }


    public static void refresh() {
        title.getText().clear();
        date.getText().clear();
        time.getText().clear();
        event_category.getText().clear();
        venue.getText().clear();
        by.getText().clear();
        minutes.getText().clear();

    }


    public void setthedata() throws IOException, DocumentException {
        acroFields.setField(fieldnames.get(0), title.getText().toString());
        acroFields.setField(fieldnames.get(1), event_category.getText().toString());
        acroFields.setField(fieldnames.get(2), date.getText().toString());
        acroFields.setField(fieldnames.get(3), time.getText().toString());
        acroFields.setField(fieldnames.get(4), venue.getText().toString());
        acroFields.setField(fieldnames.get(5), by.getText().toString());
        acroFields.setField(fieldnames.get(6), minutes.getText().toString());

    }


    public void checkfill()
    {
        if(title.getText().toString().trim().equals("")) {
            title.setError("Please input report name");
            title.requestFocus();
            Toast.makeText(getActivity().getApplicationContext(),"Please enter name for report",Toast.LENGTH_SHORT).show();
        }
        else
            writetopdf();
    }


    public void sendemail()
    {
        if(flag==0)
        {
            Toast.makeText(getActivity().getApplicationContext(), "Please Create the Pdf first", Toast.LENGTH_SHORT).show();
        }
        else {

            File filelocation = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/pdf_report/" + title.getText().toString().trim() + ".pdf");
            Uri path;
            path = FileProvider.getUriForFile(getActivity().getApplicationContext(), getActivity().getPackageName(), filelocation);
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
// set the type to 'email'
            emailIntent.setType("vnd.android.cursor.dir/email");

            emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            emailIntent.putExtra(Intent.EXTRA_STREAM, path);
// the mail subject
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Here is the Branch Visit Report - Human Resources");
            startActivity(Intent.createChooser(emailIntent, "Send email..."));

        }
    }



    public void anim(CircleButton button) {

        final Animation myAnim = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.bounce);
        boumceinter inter = new boumceinter(0.2, 20);
        myAnim.setInterpolator(inter);
        button.startAnimation(myAnim);

    }






    //---------------------------------------------------------------------------------------------------------
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.submit:
                checkfill();
                anim(submit);
                break;

            case R.id.email:
                sendemail();
                anim(email);
                break;

        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
