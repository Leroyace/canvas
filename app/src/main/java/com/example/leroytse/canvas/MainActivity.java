package com.example.leroytse.canvas;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;
import java.util.UUID;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private DrawView drawView;
    private ImageButton currPaint,penBtn,rectBtn,triangleBtn,circleBtn,saveBtn,newBtn,exitBtn,shareBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawView = (DrawView)findViewById(R.id.drawing);
        LinearLayout paintLayout = (LinearLayout)findViewById(R.id.paint_colors);
        currPaint = (ImageButton)paintLayout.getChildAt(0);
        currPaint.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.paint_pressed, null));
        penBtn = (ImageButton)findViewById(R.id.pen_btn);
        penBtn.setOnClickListener(this);
        rectBtn = (ImageButton)findViewById(R.id.rect_btn);
        rectBtn.setOnClickListener(this);

        circleBtn = (ImageButton)findViewById(R.id.circle_btn);
        circleBtn.setOnClickListener(this);
        triangleBtn = (ImageButton)findViewById(R.id.triangle_btn);
        triangleBtn.setOnClickListener(this);
        saveBtn = (ImageButton)findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(this);
        newBtn = (ImageButton)findViewById(R.id.new_btn);
        newBtn.setOnClickListener(this);
        exitBtn = (ImageButton)findViewById(R.id.exit_btn);
        exitBtn.setOnClickListener(this);
        shareBtn = (ImageButton)findViewById(R.id.share_btn);
        shareBtn.setOnClickListener(this);

    }
    public void paintClicked(View view){
        if(view!=currPaint) {
            //update color
            ImageButton imgView = (ImageButton) view;
            String col = view.getTag().toString();
            drawView.setCol(col);
            imgView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.paint_pressed, null));
            currPaint.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.paint, null));
            currPaint=(ImageButton)view;

        }
    }
    private void shareIt() {

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        sendIntent.setType("image/png");
        startActivity(sendIntent);
    }
    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.pen_btn){
            drawView.penClicked(true);
        }
        if(view.getId()==R.id.triangle_btn){
            drawView.triangleClicked(true);
        }
        if(view.getId()==R.id.circle_btn){
            drawView.circleClicked(true);
        }
        if(view.getId()==R.id.rect_btn){
            drawView.rectangleClicked(true);
        }
        if(view.getId()==R.id.new_btn){
            AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
            newDialog.setTitle("Clear");
            newDialog.setMessage("All drawings will be deleted.");
            newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    drawView.startNew();
                    dialog.dismiss();
                }
            });
            newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            newDialog.show();
        }
        if(view.getId()==R.id.save_btn){
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
            saveDialog.setTitle("Save drawing");
            saveDialog.setMessage("Save drawing to device Gallery?");
            saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    //save drawing
                    drawView.setDrawingCacheEnabled(true);
                    String imgSaved = MediaStore.Images.Media.insertImage(
                            getContentResolver(), drawView.getDrawingCache(),
                            UUID.randomUUID().toString()+".png", "drawing");
                    if(imgSaved!=null){
                        Toast savedToast = Toast.makeText(getApplicationContext(),
                                "Drawing saved to Gallery!", Toast.LENGTH_SHORT);
                        savedToast.show();
                    }
                    else{
                        Toast unsavedToast = Toast.makeText(getApplicationContext(),
                                "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
                        unsavedToast.show();
                    }
                }
            });
            saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            saveDialog.show();
        }
        if(view.getId()==R.id.exit_btn){

            AlertDialog.Builder exitDialog = new AlertDialog.Builder(this);
            exitDialog.setTitle("Exit drawing");
            exitDialog.setMessage("Do you want to exit drawing?");
            exitDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    //exit drawing
                    finish();
                    System.exit(0);
                }
            });
            exitDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            exitDialog.show();
        }
        if(view.getId()==R.id.share_btn) {
            shareIt();
        }
    }
}
