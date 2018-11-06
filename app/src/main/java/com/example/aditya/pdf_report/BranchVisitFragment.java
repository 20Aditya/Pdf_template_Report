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
import android.widget.Button;
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
import java.util.Objects;
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
    static int flag = 0;
    PdfReader reader ;
    String[] account = { "RSM HR", "Branch Manager", "Cluster Head", "Circle Head", "Line + HR", "HR Operations", "ER", "IT", "Admin", "Infra", "Marketing", "WBO", "TPP", "Retail Assets", "Business Assets" };
    static EditText dob,branchcode,branchmanager,branchname,clusterhead,staffaction,sifaarishaction;
    static EditText staraction,lateaction,bhiaction,superaction;
    static EditText newaction,attritionaction,slfaction,mandaction,vbmsaction,trainingaction;
    static EditText pacaction,roleaction,employeefeed,discipline,other;

    static BetterSpinner vbmsaccount,trainingaccount,pacaccount,roleaccount,slfaccount;
    static BetterSpinner staffaccount,sifaarishaccount,staraccount,lateaccount,bhiaccount,superaccount,newaccount,attritionaccount,mandaccount;
    ArrayList<BetterSpinner> arrayList = new ArrayList<BetterSpinner>();
    ArrayList<String> fieldnames = new ArrayList<String>();
    ArrayList<BetterSpinner> accountablity = new ArrayList<BetterSpinner>();

    AcroFields acroFields;
    Button submit,email;
    String d,s;



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




        ArrayAdapter<String> accountadapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),android.R.layout.simple_dropdown_item_1line, account
        );
        initialiseviews(view);



        //Set the number of characters the user must type before the drop down list is shown
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


        submit = (Button)view.findViewById(R.id.submit);

        email = (Button)view.findViewById(R.id.email);


        submit.setOnClickListener(this);
        email.setOnClickListener(this);



        return view;
    }


    //------------------------------------------------------------------------------------------------------

    public void anim(Button button) {

        final Animation myAnim = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.bounce);
        boumceinter inter = new boumceinter(0.2, 20);
        myAnim.setInterpolator(inter);
        button.startAnimation(myAnim);

    }


    public void initialiseviews(View view)
    {


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

        File f1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/pdf_report/branch_visit_reports");

        if(f1.isDirectory())
            Log.d("Main", "writetopdf: " + f1.isDirectory());
        else {
            f1.mkdir();
            Log.d("Main", "writetopdf: " + f1.isDirectory());
        }


        d = dob.getText().toString().trim();
        s = "";
        for(int i=0;i<d.length();i++)
        {
            if(d.charAt(i)=='/')
                continue;

            s = s + d.charAt(i);
        }



        File pdf = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/pdf_report/branch_visit_reports/" + branchname.getText().toString().trim() + "_"+ branchcode.getText().toString().trim()+"_"+s + ".pdf");
        OutputStream output = null;
        try {
            output = new FileOutputStream(pdf);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        try {
            reader = new PdfReader( getResources().openRawResource(R.raw.branchvisit) );
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            stamper = new PdfStamper(reader, output);
        } catch (DocumentException | IOException e) {
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
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }


        stamper.setFormFlattening(true);
        try {
            stamper.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }


        Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(),"PDF Generated", Toast.LENGTH_LONG).show();


    }



    public static void refresh(){
        dob.getText().clear();
        branchcode.getText().clear();
        branchname.getText().clear();
        branchmanager.getText().clear();
        clusterhead.getText().clear();

        staffaction.getText().clear();
        staffaccount.getText().clear();

        sifaarishaction.getText().clear();
        sifaarishaccount.getText().clear();

        staraction.getText().clear();
        staraccount.getText().clear();

        lateaction.getText().clear();
        lateaccount.getText().clear();

        bhiaction.getText().clear();
        bhiaccount.getText().clear();

        superaction.getText().clear();
        superaccount.getText().clear();

        newaction.getText().clear();
        newaccount.getText().clear();

        attritionaction.getText().clear();
        attritionaccount.getText().clear();

        slfaction.getText().clear();
        slfaccount.getText().clear();

        mandaction.getText().clear();
        mandaccount.getText().clear();

        vbmsaction.getText().clear();
        vbmsaccount.getText().clear();

        trainingaction.getText().clear();
        trainingaccount.getText().clear();


        pacaction.getText().clear();
        pacaccount.getText().clear();

        roleaction.getText().clear();
        roleaccount.getText().clear();
        employeefeed.getText().clear();
        discipline.getText().clear();
        other.getText().clear();


        flag=0;

    }
    public void setthedata() throws IOException, DocumentException {
        acroFields.setField(fieldnames.get(0),dob.getText().toString());
        acroFields.setField(fieldnames.get(1),branchcode.getText().toString());
        acroFields.setField(fieldnames.get(2),branchname.getText().toString());
        acroFields.setField(fieldnames.get(3),branchmanager.getText().toString());
        acroFields.setField(fieldnames.get(4),clusterhead.getText().toString());

        acroFields.setField(fieldnames.get(5),staffaction.getText().toString());
        acroFields.setField(fieldnames.get(6),staffaccount.getText().toString());

        acroFields.setField(fieldnames.get(7),sifaarishaction.getText().toString());
        acroFields.setField(fieldnames.get(8),sifaarishaccount.getText().toString());

        acroFields.setField(fieldnames.get(9),staraction.getText().toString());
        acroFields.setField(fieldnames.get(10),staraccount.getText().toString());

        acroFields.setField(fieldnames.get(11),lateaction.getText().toString());
        acroFields.setField(fieldnames.get(12),lateaccount.getText().toString());

        acroFields.setField(fieldnames.get(13),bhiaction.getText().toString());
        acroFields.setField(fieldnames.get(14),bhiaccount.getText().toString());

        acroFields.setField(fieldnames.get(15),superaction.getText().toString());
        acroFields.setField(fieldnames.get(16),superaccount.getText().toString());

        acroFields.setField(fieldnames.get(17),newaction.getText().toString());
        acroFields.setField(fieldnames.get(18),newaccount.getText().toString());

        acroFields.setField(fieldnames.get(19),attritionaction.getText().toString());
        acroFields.setField(fieldnames.get(20),attritionaccount.getText().toString());

        acroFields.setField(fieldnames.get(21),slfaction.getText().toString());
        acroFields.setField(fieldnames.get(22),slfaccount.getText().toString());

        acroFields.setField(fieldnames.get(23),mandaction.getText().toString());
        acroFields.setField(fieldnames.get(24),mandaccount.getText().toString());

        acroFields.setField(fieldnames.get(25),vbmsaction.getText().toString());
        acroFields.setField(fieldnames.get(26),vbmsaccount.getText().toString());

        acroFields.setField(fieldnames.get(27),trainingaction.getText().toString());
        acroFields.setField(fieldnames.get(28),trainingaccount.getText().toString());

        acroFields.setField(fieldnames.get(29),pacaction.getText().toString());
        acroFields.setField(fieldnames.get(30),pacaccount.getText().toString());

        acroFields.setField(fieldnames.get(31),roleaction.getText().toString());
        acroFields.setField(fieldnames.get(32),roleaccount.getText().toString());
        acroFields.setField(fieldnames.get(33),employeefeed.getText().toString());
        acroFields.setField(fieldnames.get(34),discipline.getText().toString());
        acroFields.setField(fieldnames.get(35),other.getText().toString());



    }






    public void checkfill()
    {
        if(branchname.getText().toString().trim().equals("") ||branchcode.getText().toString().trim().equals("")||dob.getText().toString().trim().equals("") ) {
            {
                if(branchname.getText().toString().trim().equals(""))
                {
                    branchname.setError("Input Branch Name");
                    branchname.requestFocus();
                }
                if(branchcode.getText().toString().trim().equals(""))
                {
                    branchcode.setError("Input Branch Code");
                    branchcode.requestFocus();
                }
                if(dob.getText().toString().trim().equals(""))
                {
                    dob.setError("Input Date");
                    dob.requestFocus();
                }
            }
            Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(),"Please enter all highlighted fields",Toast.LENGTH_SHORT).show();
        }
        else
            writetopdf();
    }


    public void sendemail()
    {
        if(flag==0)
        {
            Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "Please Create the Pdf first", Toast.LENGTH_SHORT).show();
        }
        else {

            File filelocation = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/pdf_report/branch_visit_reports/" + branchname.getText().toString().trim() + "_"+ branchcode.getText().toString().trim()+"_"+s + ".pdf");
            Uri path;
            path = FileProvider.getUriForFile(Objects.requireNonNull(getActivity()).getApplicationContext(), getActivity().getPackageName(), filelocation);
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
// set the type to 'email'
            emailIntent.setType("vnd.android.cursor.dir/email");

            emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            emailIntent.putExtra(Intent.EXTRA_STREAM, path);
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Dear ,\n" +
                    "\n" +
                    "Basis my visit to "+ branchname.getText().toString().trim() + " Branch on " + dob.getText().toString().trim() + ", please find attached the HR Branch Visit Report. \n" +
                    "\n" +
                    "The report contains two parts :\n" +
                    "\n" +
                    "1. Branch Performance against HR Parameters, action plan and accountability \n" +
                    "\n" +
                    "2.  Critical Observations \n" +
                    "\n" +
                    "This report will help you to understand and more effectively tackle the people challenges and to position the Branch for continued growth and achievement of the goals and objectives.\n" +
                    "\n" +
                    "Regards,\n" +
                    "\n" +
                    "....\n" +
                    "\n" +
                    "Human Resources");
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Here is the Branch Visit Report - Human Resources");
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
