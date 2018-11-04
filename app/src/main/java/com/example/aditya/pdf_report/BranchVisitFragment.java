package com.example.aditya.pdf_report;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BranchVisitFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BranchVisitFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BranchVisitFragment extends Fragment implements View.OnClickListener {


    PdfStamper stamper;
    int flag = 0;
    PdfReader reader ;
    String[] ratings = { "1", "2","3", "4", "5" };
    String[] account = { "RSM HR", "Branch Manager", "Cluster Head", "Circle Head", "Line + HR", "HR Operations", "ER", "IT", "Admin", "Infra", "Marketing", "WBO", "TPP", "Retail Assets", "Business Assets" };
    static EditText dob,branchcode,branchmanager,branchname,clusterhead,staffaction,sifaarishaction;
    static EditText staraction,lateaction,bhiaction,superaction;
    static EditText newaction,attritionaction,slfaction,mandaction,vbmsaction,trainingaction;
    static EditText pacaction,roleaction, reportname,employeefeed,discipline,other;
    static BetterSpinner staffrate,sifaarishrate,starrate,laterate,bhirate,superrate,newrate,attritionrate,slfrate,mandarate,vbmsrate,trainingrate;
    static BetterSpinner pacrate,rolerate,vbmsaccount,trainingaccount,pacaccount,roleaccount,slfaccount;
    static BetterSpinner staffaccount,sifaarishaccount,staraccount,lateaccount,bhiaccount,superaccount,newaccount,attritionaccount,mandaccount;
    ArrayList<BetterSpinner> arrayList = new ArrayList<BetterSpinner>();
    ArrayList<String> fieldnames = new ArrayList<String>();
    ArrayList<BetterSpinner> accountablity = new ArrayList<BetterSpinner>();

    Calendar myCalendar = Calendar.getInstance();
    AcroFields acroFields;
    CircleButton submit,email;



    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public BranchVisitFragment() {
        // Required empty public constructor
    }



    public static BranchVisitFragment newInstance(String param1, String param2) {
        BranchVisitFragment fragment = new BranchVisitFragment();
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

        View view = inflater.inflate(R.layout.fragment_branch_visit, container, false);




        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),android.R.layout.simple_dropdown_item_1line, ratings);
        ArrayAdapter<String> accountadapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),android.R.layout.simple_dropdown_item_1line, account
        );
        initialiseviews(view);



        //Set the number of characters the user must type before the drop down list is shown
        for (int i=0;i<arrayList.size();i++) {

            //Set the adapter
            arrayList.get(i).setAdapter(adapter);
        }

        for (int i=0;i<accountablity.size();i++) {

            //Set the adapter
            accountablity.get(i).setAdapter(accountadapter);
        }



        dob.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getFragmentManager(), "DatePicker");
            }
        });


        submit = (CircleButton)view.findViewById(R.id.submit);

        email = (CircleButton)view.findViewById(R.id.email);
        submit.setOnClickListener(this);
        email.setOnClickListener(this);



        return view;
    }


    //------------------------------------------------------------------------------------------------------

    public void anim(CircleButton button) {

        final Animation myAnim = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.bounce);
        boumceinter inter = new boumceinter(0.2, 20);
        myAnim.setInterpolator(inter);
        button.startAnimation(myAnim);

    }


    public void initialiseviews(View view)
    {
        staffrate = (BetterSpinner)view.findViewById(R.id.staff_rating);
        arrayList.add(staffrate);
        sifaarishrate = (BetterSpinner)view.findViewById(R.id.sifarish_rating);
        arrayList.add(sifaarishrate);
        starrate = (BetterSpinner)view.findViewById(R.id.star_rating);
        arrayList.add(starrate);
        laterate = (BetterSpinner)view.findViewById(R.id.Late_rating);
        arrayList.add(laterate);
        bhirate = (BetterSpinner)view.findViewById(R.id.bhi_rating);
        arrayList.add(bhirate);
        superrate = (BetterSpinner)view.findViewById(R.id.supervisory_rating);
        arrayList.add(superrate);
        newrate = (BetterSpinner)view.findViewById(R.id.new_joinee_rating);
        arrayList.add(newrate);
        attritionrate = (BetterSpinner)view.findViewById(R.id.attrition_rating);
        arrayList.add(attritionrate);
        slfrate = (BetterSpinner)view.findViewById(R.id.slf_rating);
        arrayList.add(slfrate);
        mandarate = (BetterSpinner)view.findViewById(R.id.mand_leave_rating);
        arrayList.add(mandarate);
        vbmsrate = (BetterSpinner)view.findViewById(R.id.vbms_rating);
        arrayList.add(vbmsrate);
        trainingrate = (BetterSpinner)view.findViewById(R.id.training_rating);
        arrayList.add(trainingrate);
        pacrate = (BetterSpinner)view.findViewById(R.id.pac_rating);
        arrayList.add(pacrate);
        rolerate = (BetterSpinner)view.findViewById(R.id.role_elevation_rating);
        arrayList.add(rolerate);

        staffaction = (EditText)view.findViewById(R.id.staff_action_plan);
        sifaarishaction = (EditText)view.findViewById(R.id.sifarish_action_plan);
        staraction = (EditText)view.findViewById(R.id.star_action_plan);
        lateaction = (EditText)view.findViewById(R.id.Late_action_plan);
        bhiaction = (EditText)view.findViewById(R.id.bhi_action_plan);
        superaction = (EditText)view.findViewById(R.id.supervisory_action_plan);
        newaction = (EditText)view.findViewById(R.id.new_joinee_action_plan);
        attritionaction = (EditText)view.findViewById(R.id.attrition_action_plan);
        slfaction = (EditText)view.findViewById(R.id.slf_action_plan);
        mandaction = (EditText)view.findViewById(R.id.mand_leave_action_plan);
        vbmsaction = (EditText)view.findViewById(R.id.vbms_action_plan);
        trainingaction = (EditText)view.findViewById(R.id.training_action_plan);
        pacaction = (EditText)view.findViewById(R.id.pac_action_plan);
        roleaction = (EditText)view.findViewById(R.id.role_elevation_action_plan);


        staffaccount = (BetterSpinner)view.findViewById(R.id.staff_accountability);
        accountablity.add(staffaccount);
        sifaarishaccount = (BetterSpinner)view.findViewById(R.id.sifarish_accountability);
        accountablity.add(sifaarishaccount);
        staraccount = (BetterSpinner)view.findViewById(R.id.star_accountability);
        accountablity.add(staraccount);
        lateaccount = (BetterSpinner)view.findViewById(R.id.Late_accountability);
        accountablity.add(lateaccount);
        bhiaccount = (BetterSpinner)view.findViewById(R.id.bhi_accountability);
        accountablity.add(bhiaccount);
        superaccount = (BetterSpinner)view.findViewById(R.id.supervisory_accountability);
        accountablity.add(superaccount);
        newaccount = (BetterSpinner)view.findViewById(R.id.new_joinee_accountability);
        accountablity.add(newaccount);
        attritionaccount = (BetterSpinner)view.findViewById(R.id.attrition_accountability);
        accountablity.add(attritionaccount);
        slfaccount = (BetterSpinner)view.findViewById(R.id.slf_accountability);
        accountablity.add(slfaccount);
        mandaccount = (BetterSpinner)view.findViewById(R.id.mand_leave_accountability);
        accountablity.add(mandaccount);
        vbmsaccount = (BetterSpinner)view.findViewById(R.id.vbms_accountability);
        accountablity.add(vbmsaccount);
        trainingaccount = (BetterSpinner)view.findViewById(R.id.training_accountability);
        accountablity.add(trainingaccount);
        pacaccount = (BetterSpinner)view.findViewById(R.id.pac_accountability);
        accountablity.add(pacaccount);
        roleaccount = (BetterSpinner)view.findViewById(R.id.role_elevation_accountability);
        accountablity.add(roleaccount);


        reportname = (EditText)view.findViewById(R.id.title);

        dob = (EditText)view.findViewById(R.id.date);
        branchcode = (EditText)view.findViewById(R.id.branch_code);
        branchname = (EditText)view.findViewById(R.id.branch_name);
        branchmanager = (EditText)view.findViewById(R.id.branch_manager);
        clusterhead = (EditText)view.findViewById(R.id.cluster_head);
        employeefeed = (EditText)view.findViewById(R.id.employee_feedback);
        discipline = (EditText)view.findViewById(R.id.discipline);
        other = (EditText)view.findViewById(R.id.other);


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

        File pdf = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/pdf_report/" + reportname.getText().toString().trim() + ".pdf");
        OutputStream output = null;
        try {
            output = new FileOutputStream(pdf);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        try {
            reader = new PdfReader( getResources().openRawResource(R.raw.finaltemplate) );
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



    public static void refresh(){
        dob.getText().clear();
        branchcode.getText().clear();
        branchname.getText().clear();
        branchmanager.getText().clear();
        clusterhead.getText().clear();
        staffrate.getText().clear();
        staffaction.getText().clear();
        staffaccount.getText().clear();
        sifaarishrate.getText().clear();
        sifaarishaction.getText().clear();
        sifaarishaccount.getText().clear();
        starrate.getText().clear();
        staraction.getText().clear();
        staraccount.getText().clear();
        laterate.getText().clear();
        lateaction.getText().clear();
        lateaccount.getText().clear();
        bhirate.getText().clear();
        bhiaction.getText().clear();
        bhiaccount.getText().clear();
        superrate.getText().clear();
        superaction.getText().clear();
        superaccount.getText().clear();
        newrate.getText().clear();
        newaction.getText().clear();
        newaccount.getText().clear();
        attritionrate.getText().clear();
        attritionaction.getText().clear();
        attritionaccount.getText().clear();
        slfrate.getText().clear();
        slfaction.getText().clear();
        slfaccount.getText().clear();
        mandarate.getText().clear();
        mandaction.getText().clear();
        mandaccount.getText().clear();
        vbmsrate.getText().clear();
        vbmsaction.getText().clear();
        vbmsaccount.getText().clear();
        trainingrate.getText().clear();
        trainingaction.getText().clear();
        trainingaccount.getText().clear();
        pacrate.getText().clear();
        pacaction.getText().clear();
        pacaccount.getText().clear();
        rolerate.getText().clear();
        roleaction.getText().clear();
        roleaccount.getText().clear();
        employeefeed.getText().clear();
        discipline.getText().clear();
        other.getText().clear();

        reportname.getText().clear();

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


        int sum=0;
        for (int i=0;i<arrayList.size();i++)
        {
            int j=0;
            if(!arrayList.get(i).getText().toString().trim().equals("")) {
                j = Integer.parseInt(arrayList.get(i).getText().toString().trim());
            }
            sum+=j;
        }
        int ans = sum/(arrayList.size());
        acroFields.setField(fieldnames.get(50),String.valueOf(ans)) ;
    }






    public void checkfill()
    {
        if(reportname.getText().toString().trim().equals("")) {
            reportname.setError("Please input report name");
            reportname.requestFocus();
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

            File filelocation = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/pdf_report/" + reportname.getText().toString().trim() + ".pdf");
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








    //-------------------------------------------------------------------------------------------------------

    // TODO: Rename method, update argument and hook method into UI event
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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }






}
