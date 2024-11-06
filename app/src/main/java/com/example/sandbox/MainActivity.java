package com.example.sandbox;

import android.app.AlertDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ScrollView scvUsers;
    EditText edtUserName;
    String userName;
    String section;
    List<String> projectList;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();

        findViewById(R.id.btnNewUser).setOnClickListener(this::createNewUserNameDialog);
        findViewById(R.id.btnEraseAll).setOnClickListener(this::createEraseAllDialog);
    }

    /**
     * Saves the actual state of objects when phone is flipped.
     *
     * @param outState Bundle in which to place your saved state.
     *
     */
    @Override
    protected void onSaveInstanceState(Bundle outState){    // CORREGIR -> SOLO GUARDA UN "BLOQUE"
        super.onSaveInstanceState(outState);

        /*
        outState.putString("userName", userName);
        outState.putString("section", section);
        outState.putStringArrayList("projectList", new ArrayList<>(projectList));
        */
    }

    /**
     * Restores the state of objects when phone is flipped.
     *
     * @param savedInstance the data most recently supplied in {@link #onSaveInstanceState}.
     *
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstance){    // CORREGIR -> SOLO DEVUELVE UN "BLOQUE"
        super.onRestoreInstanceState(savedInstance);

        /*
        userName = savedInstance.getString("userName", "");
        section = savedInstance.getString("section", "");
        projectList = savedInstance.getStringArrayList("projectList");

        if (!TextUtils.isEmpty(userName) && !projectList.isEmpty()) {
            createNewUserBlock();
        }
         */
    }

    /**
     * Method that assembles Java Views to XML Views.
     */
    protected void initComponents(){
        scvUsers = findViewById(R.id.scvUsers);
        linearLayout = findViewById(R.id.linearLayout);

        userName = "";
        section = "";
    }

    /**
     * Creates an Array of TextView and an Array of String and initializes them
     */
    protected void createNewUserBlock() {
        TextView[] arrayTextViewBlock = new TextView[6];
        String[] arrayStringBlock = {
                getString(R.string.lbl_name),
                getUserName(),
                getString(R.string.lbl_section),
                getUserSection(),
                getString(R.string.lbl_project),
                TextUtils.join(", ", getUserProjectList())
        };

        for (int i = 0; i < arrayStringBlock.length; i++) {
            arrayTextViewBlock[i] = new TextView(this);
            arrayTextViewBlock[i].setText(arrayStringBlock[i]);

            if(i == 0 || i == 2 || i == 4){
                arrayTextViewBlock[i].setTypeface(null, Typeface.BOLD);
            }

            linearLayout.addView(arrayTextViewBlock[i]);
        }

        View separator = new View(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                2 // altura de la línea en píxeles
        );
        params.setMargins(0, 16, 0, 16); // márgenes opcionales (arriba, abajo)
        separator.setLayoutParams(params);
        separator.setBackgroundColor(getResources().getColor(android.R.color.darker_gray)); // color de la línea

        linearLayout.addView(separator);
    }

    /**
     * Creates a new AlertDialog with an EditText to set up user's name. PositiveButton sets
     * user's name and opens a new AlertDialog with the following steps.
     *
     * @param v The Button that is clicked
     */
    protected void createNewUserNameDialog(View v){
        edtUserName = new EditText(this);
        edtUserName.setHint(getString(R.string.hint_user_name));
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.title_user_name))
                .setView(edtUserName)
                .setNegativeButton(getString(R.string.btn_cancel), null)
                .setPositiveButton(getString(R.string.btn_next), (dialog, which) -> {
                    setUserName(edtUserName);
                    createNewUserSectionDialog();
                })
                .create()
                .show();
    }

    /**
     * Creates a new AlertDialog with RadioButton where one must be selected. Once there is a
     * RadioButton selected, it sets the "Section" of the user. PositiveButton opens a new
     * AlertDialog with the following steps.
     */
    protected void createNewUserSectionDialog(){
        String[] arraySections = {
                getString(R.string.rbt_technology),
                getString(R.string.rbt_marketing),
                getString(R.string.rbt_security)
        };

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.title_section))
                .setSingleChoiceItems(arraySections, -1, (dialog, which) -> setUserSection(arraySections, which))
                .setNegativeButton(getString(R.string.btn_cancel), null)
                .setPositiveButton(getString(R.string.btn_next), (dialog, which) -> createNewUserProjectDialog())
                .create()
                .show();
    }

    /**
     * Creates a new AlertDialog with projects' CheckBoxes. Once PositiveButton is clicked it
     * creates a new "block" of user's data.
     */
    protected void createNewUserProjectDialog(){
        String[] arrayProjects = {
                getString(R.string.chk_wear_os),
                getString(R.string.chk_android),
                getString(R.string.chk_apple),
                getString(R.string.chk_windows)
        };
        boolean[] checkedItems = new boolean[arrayProjects.length];

        new AlertDialog.Builder(this)
                .setTitle(R.string.title_project)
                .setMultiChoiceItems(arrayProjects, checkedItems, (dialog, which, isChecked) -> {
                    checkedItems[which] = isChecked;
                    setUserProject(arrayProjects, checkedItems);
                })
                .setNegativeButton(getString(R.string.btn_cancel), null)
                .setPositiveButton(getString(R.string.btn_accept), (dialog, which) -> createNewUserBlock())
                .create()
                .show();
    }

    /**
     * Creates a new AlertDialog for erasing all added TextView in the ScrollView. PositiveButton
     * calls the method for such purpose.
     *
     * @param v The Button that is clicked.
     */
    protected void createEraseAllDialog(View v){
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.title_erase_all))
                .setNegativeButton(getString(R.string.btn_cancel), null)
                .setPositiveButton(getString(R.string.btn_accept), (dialog, which) -> eraseAll())
                .create()
                .show();
    }

    /**
     * Erase all Views in the ScrollView.
     */
    protected void eraseAll(){
        linearLayout.removeAllViews();
    }

    /**
     * Sets the user's name.
     *
     * @param edtUserName The View (EditText) within the name.
     */
    protected void setUserName(EditText edtUserName){
        userName = edtUserName.getText().toString();
    }

    /**
     * Gets the user's name.
     *
     * @return The user's name.
     */
    protected String getUserName(){
        return userName;
    }

    /**
     * Sets the user's section whenever is a RadioButton is clicked on the AlertDialog.
     *
     * @param arraySections The array that contains sections.
     * @param idSelectedItem The id of the Button clicked.
     */
    protected void setUserSection(String[] arraySections, int idSelectedItem){
        section = arraySections[idSelectedItem];
    }

    /**
     * Gets the user's section.
     *
     * @return The user's section
     */
    protected String getUserSection(){
        return section;
    }

    /**
     * Sets the user's project selection. Adds to the List<> the projects that are selected.
     *
     * @param arrayProjects The Array that contains projects.
     * @param checkedItems The Array that contains which project is selected.
     */
    protected void setUserProject(String[] arrayProjects, boolean[] checkedItems){
        projectList = new ArrayList<>();

        for (int i = 0; i < checkedItems.length; i++){
            if (checkedItems[i]){
                projectList.add(arrayProjects[i]);
            }
        }
    }

    /**
     * Gets the user's project list.
     *
     * @return The user's project list.
     */
    protected List<String> getUserProjectList(){
        return projectList;
    }
}