package com.inkriti.questionbank;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import java.util.HashMap;


/**
 * A login screen that offers login via email/password.
 */
public class AddQuestionActivity extends AppCompatActivity {
    private HashMap<String, Boolean> selectedSubjects;
    private HashMap<String, Boolean> selectedBoards;

    /**
     * Keep track of the save task to ensure we can cancel it if requested.
     */
    private SaveQuestionTask mSaveQuestionTask = null;

    // UI references.
    private String mQuestionId;
    private EditText mQuestionView;
    private EditText mAnswerView;
    private View mProgressView;
    private View mQuestionFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);
        // Set up the form.
        mQuestionView = (EditText) findViewById(R.id.question);
        mAnswerView = (EditText) findViewById(R.id.answer);
        this.selectedSubjects = new HashMap<>();
        this.selectedBoards = new HashMap<>();
        this.mQuestionId = "";

        GridView subjectsGridView = (GridView) findViewById(R.id.subjectsGridView);
        subjectsGridView.setAdapter(new SubjectAdapter(this, true));

        GridView boardsGridView = (GridView) findViewById(R.id.boardsGridView);
        boardsGridView.setAdapter(new BoardAdapter(this, true));


        Button mSaveButton = (Button) findViewById(R.id.save_button);
        mSaveButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                saveQuestion();
            }
        });

        mQuestionFormView = findViewById(R.id.question_form);
        mProgressView = findViewById(R.id.add_question_progress);

        // Handle edit scenario here
        if(getIntent().hasExtra("question")) {
            Question q = (Question) getIntent().getSerializableExtra("question");
            Log.i("edit", q.id + ":" + q.question);
            mQuestionView.setText(q.question);
            mAnswerView.setText(q.answer);
            this.mQuestionId = q.id;
            this.selectedSubjects = q.subjects;
            this.selectedBoards = q.boards;
            setTitle(R.string.title_activity_update_question);
        }else{
            setTitle(R.string.title_activity_add_question);
        }
    }

    public Boolean isSubjectSelected(String id){
        if(this.selectedSubjects.containsKey(id)){
            return true;
        }
        return false;
    }

    public Boolean isBoardSelected(String id){
        if(this.selectedBoards.containsKey(id)){
            return true;
        }
        return false;
    }

    public void selectSubjects(String id, Boolean checked){
        if(checked) {
            this.selectedSubjects.put(id, checked);
        }else{
            this.selectedSubjects.remove(id);
        }
    }

    public void selectBoards(String id, Boolean checked){
        if(checked) {
            this.selectedBoards.put(id, checked);
        }else{
            this.selectedBoards.remove(id);
        }
    }

    /**
     * Attempts to save form.
     * If there are form errors (invalid question, missing fields, etc.), the
     * errors are presented and no actual save attempt is made.
     */
    private void saveQuestion() {

        if (mSaveQuestionTask != null) {
            return;
        }

        // Reset errors.
        mQuestionView.setError(null);
        mAnswerView.setError(null);

        // Store values at the time of the login attempt.
        String question = mQuestionView.getText().toString();
        String answer = mAnswerView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(answer)) {
            mAnswerView.setError("This field is required");
            focusView = mAnswerView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(question)) {
            mQuestionView.setError("This field is required");
            focusView = mQuestionView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mSaveQuestionTask = new SaveQuestionTask(question, answer, this.selectedSubjects, this.selectedBoards);
            mSaveQuestionTask.execute((Void) null);
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mQuestionFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mQuestionFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mQuestionFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mQuestionFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class SaveQuestionTask extends AsyncTask<Void, Void, Boolean> {

        private final String mQuestion;
        private final String mAnswer;
        private final HashMap<String, Boolean> mSubjects;
        private final HashMap<String, Boolean> mBoards;

        SaveQuestionTask(String question, String answer, HashMap<String, Boolean> subjects, HashMap<String, Boolean> boards) {
            mQuestion = question;
            mAnswer = answer;
            mSubjects = subjects;
            mBoards = boards;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            this.writeNewQuestion(mQuestionId, mQuestion, mAnswer, mSubjects, mBoards);

            return true;
        }

        public void writeNewQuestion(String id, String question, String answer, HashMap<String, Boolean> subjects,
                                     HashMap<String, Boolean> boards){
            Question q = new Question(id, question, answer);
            q.setSubjects(subjects);
            q.setBoards(boards);
            q.saveQuestion();
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mSaveQuestionTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
//                mPasswordView.setError(getString(R.string.error_incorrect_password));
//                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mSaveQuestionTask = null;
            showProgress(false);
        }
    }
}

