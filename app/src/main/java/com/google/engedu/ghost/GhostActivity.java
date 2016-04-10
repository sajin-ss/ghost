package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends ActionBarActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    private SimpleDictionary simpleDictionary;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);

        Button challenge = (Button) findViewById(R.id.challenge);
        challenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView label = (TextView) findViewById(R.id.gameStatus);
                TextView word = (TextView) findViewById(R.id.word);
                String prefix = word.getText().toString();

                if (prefix.length() >= 4 && simpleDictionary.isWord(prefix.toLowerCase())) {
                    label.setText("You Win");

                }

            }
        });

        Button restart = (Button) findViewById(R.id.restart);
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView tv = (TextView) findViewById(R.id.word);
                tv.setText("");
                onStart(null);
            }
        });

        AssetManager assetManager = getAssets();

        try {
            InputStream inputStream = assetManager.open("words.txt");
            simpleDictionary = new SimpleDictionary(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        onStart(null);


    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        char ch = (char) event.getUnicodeChar();
        if(keyCode<=KeyEvent.KEYCODE_Z&&keyCode>=KeyEvent.KEYCODE_A){
            TextView word = (TextView) findViewById(R.id.word);
            word.append(String.valueOf(ch).toUpperCase());

            String curr = word.getText().toString();
            String temp = simpleDictionary.getAnyWordStartingWith(curr);

            computerTurn();
        }

        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void computerTurn() {
        TextView label = (TextView) findViewById(R.id.gameStatus);
        TextView word = (TextView) findViewById(R.id.word);
        String prefix = word.getText().toString();

        Log.d("prefix is ", prefix);

        if (prefix.length()>=4 && simpleDictionary.isWord(prefix.toLowerCase())){
            label.setText("Computer Wins");
            return;
        }
        else{
            String temp = simpleDictionary.getAnyWordStartingWith(prefix);
            if (temp == "")
            {
                label.setText("Computer Wins");
                return;
            }
            char[] t = temp.toCharArray();
            char c = t[prefix.length()];
            word.append(String.valueOf(c).toUpperCase());

        }
        // Do computer turn stuff then make it the user's turn again
        userTurn = true;
        label.setText(USER_TURN);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("user turn", userTurn);
        TextView word = (TextView) findViewById(R.id.word);
        String prefix = word.getText().toString();
        outState.putString("word", prefix);
        TextView label = (TextView) findViewById(R.id.gameStatus);
        String status =  label.getText().toString();
        outState.putString("status", status);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        userTurn = savedInstanceState.getBoolean("userTurn");
        TextView word = (TextView) findViewById(R.id.word);
        String prefix = savedInstanceState.getString("word");
        word.setText(prefix);
        TextView label = (TextView) findViewById(R.id.gameStatus);
        String status = savedInstanceState.getString("status");
        label.setText(status);
    }
}
