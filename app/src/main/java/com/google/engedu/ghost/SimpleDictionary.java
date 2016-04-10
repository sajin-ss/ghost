package com.google.engedu.ghost;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {

        if (prefix.isEmpty()){
            int i = new Random().nextInt(words.size());
            return words.get(i);
        }
        return search(prefix, 0, words.size());
    }

    public String search(String prefix, int begin, int end){

        Log.d("testing", "test");
        while (begin < end){
            Log.d("end value", String.valueOf(end));

            int middle = (begin+end)/2;

            if (words.get(middle).startsWith(prefix.toLowerCase())){

                Log.d("isPrefix", "true  " + words.get(middle));
                return words.get(middle);
            }
            if(words.get(middle).compareTo(prefix.toLowerCase())<0){
                begin = middle + 1;
                Log.d("less than", words.get(middle));
            }
            else{
                end = middle -1;
                Log.d("bigger than", words.get(middle));
            }
        }

        return "";
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        return null;
    }
}
