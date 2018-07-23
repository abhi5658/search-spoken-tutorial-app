package com.example.abhishek.searchspokentutorialapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

/*
This is the First Activity
 */
class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var fossVsFossID = HashMap<String, Int>()
        fossVsFossID.put("Java", 10)
        fossVsFossID.put("Cpp", 43)
        fossVsFossID.put("Python", 26)
        fossVsFossID.put("RDBMS", 92)
        fossVsFossID.put("Blender", 1)

        var languageVsLanguageID = HashMap<String, Int>()
        languageVsLanguageID.put("English", 22)
        languageVsLanguageID.put("Hindi", 6)
        languageVsLanguageID.put("Gujarati", 5)
        languageVsLanguageID.put("Tamil", 18)
        languageVsLanguageID.put("Marathi", 12)
        languageVsLanguageID.put("Kannada", 7)


        var fossVsLanguage = HashMap<String, ArrayList<String>>()

        var availableLanguagesForFoss: ArrayList<String>

        availableLanguagesForFoss = arrayListOf("English", "Hindi", "Gujarati", "Kannada")
        fossVsLanguage.put("Java", availableLanguagesForFoss)

        availableLanguagesForFoss = arrayListOf("English", "Hindi", "Gujarati", "Tamil", "Marathi", "Kannada")
        fossVsLanguage.put("Cpp", availableLanguagesForFoss)

        availableLanguagesForFoss = arrayListOf("English", "Hindi")
        fossVsLanguage.put("Python", availableLanguagesForFoss)

        availableLanguagesForFoss = arrayListOf("English")
        fossVsLanguage.put("RDBMS", availableLanguagesForFoss)

        availableLanguagesForFoss = arrayListOf("English", "Hindi", "Tamil", "Marathi")
        fossVsLanguage.put("Blender", availableLanguagesForFoss)


        var fossOptions = fossVsFossID.keys.toMutableList()
        fossOptions.sort()
        //initialize fossOptions with initial string
        fossOptions.add(0, "Select foss...")

        //initialize languageOptions with initial string
        var languageOptions = arrayListOf("Select Language...")

        //https://android--code.blogspot.in/2015/08/android-spinner-hint.html
        var fossAdapter = object : ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fossOptions) {

            //<<<<<<<<<<<<<<<-----Extra code starts below---------------
            override fun isEnabled(position: Int): Boolean {
                if (position == 0) {
                    return false
                } else {
                    return true
                }
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
                var view = super.getDropDownView(position, convertView, parent)
                var tv = view as TextView
                if (position == 0) {
                    tv.setTextColor(Color.GRAY)
                } else {
                    tv.setTextColor(Color.BLACK)
                }
                return view
            }
            //---------------Extra code ends here---->>>>>>>>>>>>>>>>>>>>
        }
        fossSpinner.adapter = fossAdapter

        var languageAdapter = object : ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, languageOptions) {

            //<<<<<<<<<<<<<<<-----Extra code starts below---------------
            override fun isEnabled(position: Int): Boolean {
                if (position == 0) {
                    return false
                } else {
                    return true
                }
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
                var view = super.getDropDownView(position, convertView, parent)
                var tv = view as TextView
                if (position == 0) {
                    tv.setTextColor(Color.GRAY)
                } else {
                    tv.setTextColor(Color.BLACK)
                }
                return view
            }
            //---------------Extra code ends here---->>>>>>>>>>>>>>>>>>>>
        }
        languageSpinner.adapter = languageAdapter

        var selectedFoss = "None"
        var selectedLanguage = "None"

        fossSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(p0: AdapterView<*>?) {
                selectedFoss = "None"
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                languageOptions.clear()

                if (position == 0) {
                    selectedFoss = "none"
                    languageOptions.add("Select a foss first!")

                } else {

                    selectedFoss = fossOptions.get(position)

                    if (fossVsLanguage.containsKey(fossOptions.get(position))) {

                        languageOptions.addAll(ArrayList(fossVsLanguage.get(fossOptions.get(position))))
                        languageOptions.sort()
                        languageOptions.add(0, "[Select]")
                    } else {
                        languageOptions.add("No available languages")
                    }
                }

                languageAdapter.notifyDataSetChanged()
                languageSpinner.setSelection(0)
            }
        }

        languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(p0: AdapterView<*>?) {
                selectedLanguage = "None"
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 0) {
                    selectedLanguage = "none"
                } else {
                    selectedLanguage = languageOptions.get(position)
                }
            }
        }

        searchButton.setOnClickListener {

            if (selectedFoss == "none" || selectedLanguage == "none") {
                Toast.makeText(this, "Please select FOSS and Language both", Toast.LENGTH_SHORT).show()
            } else {

                var searchIntent = Intent(this, SearchActivity::class.java).apply {
                    putExtra("foss", "" + selectedFoss)
                    putExtra("fossID", "" + fossVsFossID.get(selectedFoss))

                    putExtra("language", "" + selectedLanguage)
                    putExtra("languageID", "" + languageVsLanguageID.get(selectedLanguage))
                }
                startActivity(searchIntent)
            }
        }
    }
}
