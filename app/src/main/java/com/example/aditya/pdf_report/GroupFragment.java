package com.example.aditya.pdf_report;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BadPdfFormatException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImage;
import com.itextpdf.text.pdf.PdfIndirectObject;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PushbuttonField;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import at.markushi.ui.CircleButton;

import static android.app.Activity.RESULT_OK;


public class GroupFragment extends Fragment implements View.OnClickListener {


    View view;
    static File pdf,f,f1;
    static PdfStamper stamper;
    static int flag = 0;
    static PdfReader reader ;
    static EditText title,date,time,venue,by,minutes;
    static BetterSpinner event_category;
    String[] events = { "NJ Meet", "Coffee with Top Kats", "TP Meet",  "Open Forum", "Town Hall", "Mentoship Meet", "Focused Group Discussion" };

    Bitmap bitmap,bitmap2;
    Calendar myCalendar = Calendar.getInstance();
    AcroFields acroFields;
    CircleButton submit,email;
    Button upload,upload1;
    ArrayList<String> fieldnames = new ArrayList<String>();
    private int PICK_IMAGE_REQUEST = 1;
    private static  int count = 0;



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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()).getApplicationContext(),android.R.layout.simple_dropdown_item_1line, events);
        event_category.setAdapter(adapter);





        //Set Date
        date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DialogFragment newFragment = new SelectDateGroup();
                assert getFragmentManager() != null;
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

        upload.setOnClickListener(this);
        upload1.setOnClickListener(this);
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
        upload = (Button)view.findViewById(R.id.upload);
        upload1 = (Button)view.findViewById(R.id.upload1);
        submit = (CircleButton)view.findViewById(R.id.submit);
        email = (CircleButton)view.findViewById(R.id.email);
    }








    public void writetopdf() throws IOException, DocumentException {

        flag=1;


        f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/pdf_report");
        if(f.isDirectory())
            Log.d("Main", "writetopdf: " + f.isDirectory());
        else {
            f.mkdir();
            Log.d("Main", "writetopdf: " + f.isDirectory());
        }


        f1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/pdf_report/group_initiatives");
        if(f1.isDirectory())
            Log.d("Main", "writetopdf: " + f1.isDirectory());
        else {
            f1.mkdir();
            Log.d("Main", "writetopdf: " + f1.isDirectory());
        }


        pdf = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/pdf_report/group_initiatives/" + title.getText().toString().trim() + ".pdf");
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
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }



        //add image

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] imagebyte = stream.toByteArray();
            Image image = Image.getInstance(imagebyte);


            PdfImage pdfstream = new PdfImage(image, "", null);
            pdfstream.put(new PdfName("ITXT_SpecialId"), new PdfName("123456789"));
            PdfIndirectObject ref = stamper.getWriter().addToBody(pdfstream);

            image.setDirectReference(ref.getIndirectReference());

            image.setAbsolutePosition(110, 80);
            image.scaleToFit(180, 125);
            PdfContentByte over = stamper.getOverContent(1);
            over.addImage(image);


            ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
            bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, stream2);
            byte[] imagebyte2 = stream2.toByteArray();
            Image image2 = Image.getInstance(imagebyte2);


            PdfImage pdfstream2 = new PdfImage(image2, "", null);
            pdfstream.put(new PdfName("ITXT_SpecialId"), new PdfName("123456789"));
            PdfIndirectObject ref2 = stamper.getWriter().addToBody(pdfstream2);

            image2.setDirectReference(ref2.getIndirectReference());

            image2.setAbsolutePosition(320, 80);
            image2.scaleToFit(180,125);
            PdfContentByte over2 = stamper.getOverContent(1);
            over2.addImage(image2);




        Log.d("group", "Heysa " + 1);




        stamper.setFormFlattening(true);
        try {
            stamper.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }


        Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(),"PDF Generated", Toast.LENGTH_LONG).show();


    }




    public static void refresh() {
        title.getText().clear();
        date.getText().clear();
        time.getText().clear();
        event_category.getText().clear();
        venue.getText().clear();
        by.getText().clear();
        minutes.getText().clear();
        count=0;
        flag=0;
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


    public void checkfill() throws IOException, DocumentException {
        if(title.getText().toString().trim().equals("")) {
            title.setError("Please input report name");
            title.requestFocus();
            Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(),"Please enter name for report",Toast.LENGTH_SHORT).show();
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

            File filelocation = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/pdf_report/group_initiatives/" + title.getText().toString().trim() + ".pdf");
            Uri path;
            path = FileProvider.getUriForFile(Objects.requireNonNull(getActivity()).getApplicationContext(), getActivity().getPackageName(), filelocation);
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

        final Animation myAnim = AnimationUtils.loadAnimation(Objects.requireNonNull(getActivity()).getApplicationContext(), R.anim.bounce);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {



            Log.d("Report", "code23 = " + requestCode);
            Log.d("Report", "codes = " + resultCode);
            Uri uri = data.getData();

            if(uri!=null)
                Log.d("group", "onActivityResult: " + true);

            try {
                // Log.d(TAG, String.valueOf(bitmap));
                count++;
                if(count==1) {
                    bitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getActivity()).getContentResolver(), uri);
                }
                if(count==2) {
                    bitmap2 = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getActivity()).getContentResolver(), uri);
                }

            } catch (IOException e) {

                e.printStackTrace();
            }
        }
        else
        {
            Log.d("Report", "code22 = " + requestCode);
            Log.d("Report", "codes = " + resultCode);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.submit:
                try {
                    checkfill();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
                anim(submit);
                break;

            case R.id.email:
                sendemail();
                anim(email);
                break;

            case R.id.upload:
                if(count==2)
                    count=0;
                if(count==1)
                    count=0;
                Intent intent = new Intent();
                // Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                break;

            case R.id.upload1:
                if(count==2)
                    count=1;

                Intent newintent = new Intent();
                // Show only images, no videos or anything else
                newintent.setType("image/*");
                newintent.setAction(Intent.ACTION_GET_CONTENT);
                // Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(newintent, "Select Picture"), PICK_IMAGE_REQUEST);
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
