package com.example.arknightsoperatorviewer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class OperatorListActivity extends AppCompatActivity implements OperatorListRVAdapter.OnOperatorListener {

    public ArrayList<OperatorInfo> opData = new ArrayList<OperatorInfo>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operatorlist);

        //setup the sorting bar at the top of the list
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //fetch operator data from the database
        getSQLJSON("http://192.168.56.1/api/getOperators.php");
        initRecyclerView();

        //setup bottom navigation menu
        BottomNavigationView botNav = (BottomNavigationView) findViewById(R.id.bottomNavBar);
        Menu menu = botNav.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        //bottom nav menu listener
        botNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.world:
                        Intent intent0 = new Intent(OperatorListActivity.this, MainActivity.class);
                        startActivity(intent0);
                        break;
                    case R.id.operators:
                        break;
                }
                return false;
            }
        });

    }

    //initialize sorting menu and inflate it
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort_menu, menu);
        return true;
    }

    //manages list sorting logic
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.iconlowtohigh:
                Collections.sort(opData, new Comparator<OperatorInfo>(){
                    public int compare(OperatorInfo o1, OperatorInfo o2) {
                        Integer o1Star = o1.getStars();
                        Integer o2Star = o2.getStars();
                        return o1Star.compareTo(o2Star);
                    }
                });
                initRecyclerView();
                return true;
            case R.id.iconhightolow:
                Collections.sort(opData, new Comparator<OperatorInfo>(){
                    public int compare(OperatorInfo o1, OperatorInfo o2) {
                        Integer o1Star = o1.getStars();
                        Integer o2Star = o2.getStars();
                        return o2Star.compareTo(o1Star);
                    }
                });
                initRecyclerView();
                return true;
            case R.id.iconnameAZ:
                Collections.sort(opData, new Comparator<OperatorInfo>(){
                    public int compare(OperatorInfo o1, OperatorInfo o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });
                initRecyclerView();
                return true;
            case R.id.iconnameZA:
                Collections.sort(opData, new Comparator<OperatorInfo>(){
                    public int compare(OperatorInfo o1, OperatorInfo o2) {
                        return o2.getName().compareTo(o1.getName());
                    }
                });
                initRecyclerView();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //initializes the main menu RecyclerView with the opData arraylist.
    private void initRecyclerView()
    {
        RecyclerView recyclerView = findViewById(R.id.oplist);
        OperatorListRVAdapter adapter = new OperatorListRVAdapter(this, opData, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    //listener for the RecyclerView items. Starts the correct rarity activity based on the operator selected.
    @Override
    public void onOperatorClick(int position)
    {
        OperatorInfo temp = opData.get(position);
        if(temp instanceof OperatorInfo.Operator2Star) {
            //OperatorInfo.Operator3Star temp1 = opData.get(position);
            Intent intent = new Intent(this, Detailed2OpActivity.class);
            intent.putExtra("operator", (OperatorInfo.Operator2Star) opData.get(position));
            startActivity(intent);
        }
        else if(temp instanceof OperatorInfo.Operator3Star) {
            //OperatorInfo.Operator3Star temp1 = opData.get(position);
            Intent intent = new Intent(this, Detailed3OpActivity.class);
            intent.putExtra("operator", (OperatorInfo.Operator3Star) opData.get(position));
            startActivity(intent);
        }
        else if(temp instanceof OperatorInfo.Operator45Star.Operator6Star)
        {
            Intent intent = new Intent(this, Detailed6OpActivity.class);
            intent.putExtra("operator", (OperatorInfo.Operator45Star.Operator6Star) opData.get(position));
            startActivity(intent);
        }
        else if(temp instanceof OperatorInfo.Operator45Star)
        {
            Intent intent = new Intent(this, DetailedOpActivity.class);
            intent.putExtra("operator", (OperatorInfo.Operator45Star) opData.get(position));
            startActivity(intent);
        }
    }

    //NOW DEPRECATED: file reading initialization of data.
    //If viewing from GitHub, please use this init in the onCreate method rather than
    //calling the getSQLJSON() method because the server that I run the database on might not be on
    //at all times.
    private void initTestOperatorNames()
    {
        StringReader sr = new StringReader(this);
        String file = sr.readEntire("opdata.txt");
        //super wonky formatting. I don't know why this is the correct amount of new line breaks, but it is. Just deal with it
        String[] opArr = file.split("\n\n\n");
        for(String data : opArr)
        {
            data = data.trim();
            String[] specificArr = data.split("-=-");
            int rarity = Integer.parseInt(specificArr[0].trim());
            String name = specificArr[1].trim();
            String type = specificArr[2].trim();
            Log.d("int", data);
            String profile = specificArr[3];
            String clinical, t1, t2, t3, t4, promo, affil;
            switch(rarity)
            {
                case 2:
                    clinical = specificArr[4].trim();
                    t1 = specificArr[5].trim();
                    t2 = specificArr[6].trim();
                    t3 = specificArr[7].trim();
                    t4 = specificArr[8].trim();
                    affil = specificArr[9].trim();
                    OperatorInfo.Operator2Star op2 = new OperatorInfo.Operator2Star(name, type, profile, clinical, t1, t2, t3, t4, rarity, affil);
                    opData.add(op2);
                    break;
                case 3:
                    clinical = specificArr[4].trim();
                    t1 = specificArr[5].trim();
                    t2 = specificArr[6].trim();
                    t3 = specificArr[7].trim();
                    t4 = specificArr[8].trim();
                    affil = specificArr[9].trim();
                    OperatorInfo.Operator3Star op3 = new OperatorInfo.Operator3Star(name, type, profile, clinical, t1, t2, t3, t4, rarity, affil);
                    opData.add(op3);
                    break;
                case 4:
                case 5:
                    clinical = specificArr[4].trim();
                    t1 = specificArr[5].trim();
                    t2 = specificArr[6].trim();
                    t3 = specificArr[7].trim();
                    t4 = specificArr[8].trim();
                    promo = specificArr[9].trim();
                    affil = specificArr[10].trim();
                    OperatorInfo.Operator45Star op5 = new OperatorInfo.Operator45Star(name, type, profile, clinical, t1, t2, t3, t4, rarity, affil, promo);
                    opData.add(op5);
                    break;
            }
        }
    }

    //Method to help get the database info in JSON form and then parse it into Operator profiles.
    private void getSQLJSON(final String urlMYSQL)
    {
        //helper class that contains all of the background operating methods
        class GetJSON extends AsyncTask<Void, Void, String>
        {
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s)
            {
                super.onPostExecute(s);
                try
                {
                    initOperators(s);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

            //creates a connection to the database and fetches the operator profiles asynchronously
            @Override
            protected String doInBackground(Void... voids)
            {
                try
                {
                    //establish connection
                    URL url = new URL(urlMYSQL);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    //initialize reader objects to intake data
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String jsonStr;

                    //
                    while ((jsonStr = bufferedReader.readLine()) != null)
                    {
                        sb.append(jsonStr + "\n");
                    }
                    return sb.toString().trim();
                }
                catch (Exception e)
                {
                    return null;
                }
            }
        }
        GetJSON jsonObj = new GetJSON();
        jsonObj.execute();
    }

    //after data is fetched, parse it from JSON into strings to be used in operator profiles.
    private void initOperators(String json) throws JSONException
    {
        JSONArray jsonArray = new JSONArray(json);
        int rarity;
        String name, type, profile, clinical, t1, t2, t3, t4, promo, affil;
        Log.d("int", "uwu");
        for (int i = 0; i < jsonArray.length(); i++)
        {
            JSONObject obj = jsonArray.getJSONObject(i);
            rarity = obj.getInt("rarity");
            name = obj.getString("opname");
            type = obj.getString("type");
            profile = obj.getString("profile");
            switch(rarity)
            {
                case 2:
                    clinical = obj.getString("clinical");
                    t1 = obj.getString("t1");
                    t2 = obj.getString("t2");
                    t3 = obj.getString("t3");
                    t4 = obj.getString("t4");
                    affil = obj.getString("affiliation");
                    OperatorInfo.Operator2Star op2 = new OperatorInfo.Operator2Star(name, type, profile, clinical, t1, t2, t3, t4, rarity, affil);
                    opData.add(op2);
                    break;
                case 3:
                    clinical = obj.getString("clinical");
                    t1 = obj.getString("t1");
                    t2 = obj.getString("t2");
                    t3 = obj.getString("t3");
                    t4 = obj.getString("t4");
                    affil = obj.getString("affiliation");
                    OperatorInfo.Operator3Star op3 = new OperatorInfo.Operator3Star(name, type, profile, clinical, t1, t2, t3, t4, rarity, affil);
                    opData.add(op3);
                    break;
                case 4:
                case 5:
                    clinical = obj.getString("clinical");
                    t1 = obj.getString("t1");
                    t2 = obj.getString("t2");
                    t3 = obj.getString("t3");
                    t4 = obj.getString("t4");
                    affil = obj.getString("affiliation");
                    promo = obj.getString("promotion");
                    OperatorInfo.Operator45Star op5 = new OperatorInfo.Operator45Star(name, type, profile, clinical, t1, t2, t3, t4, rarity, affil, promo);
                    opData.add(op5);
                    break;
                case 6:
                    clinical = obj.getString("clinical");
                    t1 = obj.getString("t1");
                    t2 = obj.getString("t2");
                    t3 = obj.getString("t3");
                    t4 = obj.getString("t4");
                    affil = obj.getString("affiliation");
                    promo = obj.getString("promotion");
                    OperatorInfo.Operator45Star.Operator6Star op6 = new OperatorInfo.Operator45Star.Operator6Star(name, type, profile, clinical, t1, t2, t3, t4, rarity, affil, promo);
                    opData.add(op6);
                    break;
            }
        }
        //default sort the list by rarity from high to low
        Comparator<OperatorInfo> compareByRarity = new Comparator<OperatorInfo>() {
            @Override
            public int compare(OperatorInfo o1, OperatorInfo o2) {
                Integer o1Star = o1.getStars();
                Integer o2Star = o2.getStars();
                return o2Star.compareTo(o1Star);
            }
        };
        Collections.sort(opData, compareByRarity);
    }
}

    /*
    Now unused code, hardcoded operators rather than data storage

    private void initOperatorNames()
    {
        OperatorInfo.Operator45Star platinum = new OperatorInfo.Operator45Star("Platinum", "sniper",
                "Platinum was previously an assassin for the Kazimierz Armorless Union. The rest of her background is unknown. " +
                        "She has demonstrated considerable talent and special tactical accomplishments in high-mobility operations, clearance, and urban warfare.\n" +
                        "Under the leadership of Dr. Kal'tsit, she is a Sniper Operator for Rhodes Island.",
                "Imaging tests reveal clear, normal outlines of internal organs, and no abnormal shadows have been detected. Originium granules have not been detected in the circulatory system and there is no sign of infection. At this time, this Operator is believed to be non-infected.\n" +
                        "\n" +
                        "[Cell-Originium Fusion Rate] 0%\n" +
                        "Operator shows no signs of infection at this time.\n" +
                        "\n" +
                        "[Blood Crystal Density] 0.11u/L\n" +
                        "This Operator has rarely come into contact with Originium.\n" +
                        "\n" +
                        "Physical indicators have all tested at normal levels. After the conclusion of the examination process, I have taken over this Operator's relevant investigation and data files.\n" +
                        "- Kal'tsit",
                "The only information that has been confirmed about Platinum is her hometown in Kazimierz. All other information that Platinum shares must be taken with a grain of salt, including medical data like chest size and weight. Platinum prefers to falsify this data (often through threats and blackmail) as she believes\n" +
                        "\"A girl should be allowed to have her secrets!\"\n" +
                        "Therefore, most of the information in Platinum's file has no value.\n" +
                        "Of course, core records and non-public materials are still subject to close scrutiny.",
                "The Knight-Assassins are a secret military force cultivated by the various forces of Kazimierz during political struggles with rogue knights.\n" +
                        "The story goes that in order to counter the rogue knights who were well versed in combat, the Knight Assassins utilized special assassination and guerrilla combat techniques. Consequently, they launched a relentless campaign of destruction against the rogue knights that refused to cooperate with the reigning government.\n" +
                        "Although it is impossible to verify the authenticity of these rumors, Platinum does exhibit some of the skills that could have been used to fight the Kazimierz knights. Both in aspects that can't be measured as well as her measurable if incredible bow skills, under special circumstances, Platinum poses a huge threat to armored targets.\n" +
                        "In light of these findings, investigations into Platinum must continue.",
                "In the end, Platinum chose to join the team directly led by Dr. Kal'tsit in order to develop her own strengths and to prove that her relationship with Rhodes Island was genuine. This is an acceptable result for Rhodes Island.\n" +
                        "Aside from teasing other Operators, shirking her daily chores, and other minor rule violations, Platinum has proven that she has the essence of an outstanding Operator.\n" +
                        "Even so, Platinum needs to make sure she applies for \"read permission\" from management and not steal glances during the data compilation process.\n" +
                        "I hope Dr. Kal'tsit will consider putting an end to some of her more outrageous behaviors. Thank you.\n",
                "[Classified Log]\n" +
                        "Even though Platinum currently follows my instructions and orders, she seems to care little for Rhodes Island itself. Even from the perspective of the employment relationship, Rhodes Island's pay may not be able to satisfy her. As such, it is critical we seriously consider whether Platinum will remain loyal.\n" +
                        "Of course, according to my personal observations, the relationship between Platinum and Rhodes Island is mostly derived from her trust in the Doctor.\n" +
                        "In other words, as long as Platinum's relationship with the Doctor is strong enough, she will continue to hold considerable value for Rhodes Island.\n" +
                        "There are multiple ways we can go about removing the variable factors in this situation. Strengthening our relationship to dispel excessive notions of independence ought to be feasible.\n" +
                        "- Kal'tsit",
                5, "kazimierz",
                "Currently, all of the intelligence Rhodes Island has regarding the Kazimierz Armorless Union comes from Platinum herself and should not be regarded as the full truth.\n" +
                        "At the same time, when conducting secret missions into the large cities of Kazimierz, attention must be paid to the next steps the Armorless Union takes. Rhodes Island should be vigilant of other groups of unknown alignment operating in the shadows.");

        opData.add(platinum);
        OperatorInfo.Operator45Star greythroat = new OperatorInfo.Operator45Star("GreyThroat", "sniper",
                "Operator GreyThroat, despite only receiving her codename recently, has actually been part of Rhodes Island for much longer than the time she's served as an Operator. " +
                        "Her parents were formerly Oripathy researchers who travelled around. Her mother entrusted GreyThroat to Rhodes Island before disappearing. " +
                        "Now, she has passed the Operator assessment and is a member of Rhodes Island's operation team.",
                "Imaging tests reveal clear, normal outlines of internal organs, and no abnormal shadows have been detected. " +
                        "Originium granules have not been detected in the circulatory system and there are no other signs of infection. At this time, this operator can be confirmed to be uninfected.\n" +
                        "\n" +
                        "[Cell-Originium Assimilation] 0%\n" +
                        "Operator GreyThroat shows no signs of Oripathy infection.\n" +
                        "\n" +
                        "[Blood Originium-Crystal Density] 0.11u/L\n" +
                        "Operator GreyThroat has limited exposure to Originium, and undertakes complete protective measures when potentially coming in contact with Originium in her line of work.\n" +
                        "\n" +
                        "When compiling this report, unusual folds were found on the Operator's previous medical examination report. Presumably, the Operator clenched the report upon receiving it, " +
                        "which in and of itself is not a big deal. In the future, however, you should pay attention to the mental health of the Operator. --Medical Group Remarks",
                "GreyThroat's parents were both excellent Oripathy researchers and had some academic cooperation with some of Rhodes Island's medic operators. However, after a certain point, " +
                        "her father disappeared completely, and not long afterwards, her mother brought the young GreyThroat to Rhodes Island. She too disappeared after leaving her daughter behind. " +
                        "Her mental condition seemed to be extremely distressed as she left Rhodes Island. Though a messenger was later sent to try to get in contact with her, our efforts were akin to chasing a ghost.\n" +
                        "GreyThroat, at the time she first arrived at Rhodes Island, had a series of worrisome issues, most notably her resentment towards Originium and Oripathy. " +
                        "She didn't even try to hide her hate towards the Infected. At first, the Operator entrusted with taking care of her did not take her mental state to be a serious issue, given that she was still young " +
                        "and might be overwhelmed by her experiences. However, I must admit that the seeds of GreyThroat's subsequent problematic development, as well as the many conflicts she would later cause, " +
                        "were planted during this time long ago.\n\n" +
                        "(Note: The radical words used in the original file involving discrimination and hate have been redacted, and the Doctor may only refer to this file.)",
                "When Rhodes Island discovered the cause of death of GreyThroat's father, several months had already passed. During this time, GreyThroat remained isolated, " +
                        "trying to avoid contact with the various personnel within Rhodes Island. Someone once witnessed GreyThroat scraping at her own arm with a piece of industrial sandpaper, " +
                        "and was not discovered by staff until her arm had been turned into a bloody mess. The reason was because GreyThroat accidentally touched some Originium-based devices.\n" +
                        "While GreyThroat has not taken any radical behavior towards Infected persons, her actions have been a source of discontentment among many younger operators. " +
                        "The ostracized GreyThroat of course takes no issue with this fact, and continues to lead a relatively solitary life in Rhodes Island.\n" +
                        "The only exception is Amiya, one of the few people she is willing to communicate with. Perhaps due to being separated from her family at a young age, " +
                        "her desire to speak with a peer for support sometimes even eclipses her prejudices. However, even when she is in the same room with Amiya, " +
                        "she carefully maintains her distance, exhibiting mild PTSD at the very beginning. At Amiya's suggestion, and in return for adoption into Rhodes Island, " +
                        "GreyThroat began training for the Operator assessment and learned to use bows and crossbows. Some were skeptical, claiming that it was just an excuse for GreyThroat to leave Rhodes Island, " +
                        "or even try to defect, but Amiya quickly put a halt to this, as well as other unfounded rumors.",
                "After officially passing the Operator assessment exam, GreyThroat acquired her codename. Even those who are vocally opposed to her conduct cannot deny her combat performance. " +
                        "On the battlefield, GreyThroat is far keener than Operators who have served for a similar length of time, her excellent dynamic vision compensating for her lack of marksmanship experience.\n" +
                        "However, can a Rhodes Island Operator shoulder her duties while being so hostile and disgusted towards the Infected? In order to address the concerns of others, " +
                        "Amiya personally took the initiative to put GreyThroat on her operation team, and successfully carried out several missions.\n" +
                        "Some people may be hard to change, but many hoped that Amiya would be able to gradually influence this somewhat abrasive girl. But, reality would once again surprise everyone. " +
                        "After a long and harrowing rescue mission, GreyThroat changed dramatically overnight. She began to learn to talk to the Infected, " +
                        "though she still would often touch upon topics that are typically off-limits for the Infected. Her tone was cold, and sometimes even full of barbs, " +
                        "but her frankness began to soften the hearts of many.\n" +
                        "Very few people know about what exactly transpired on that mission, with Amiya and GreyThroat both seldom mentioning it. " +
                        "What is certain, is that GreyThroat is no longer blinded by a myopic hatred. However, is her heart actually free from the shackles of hate? " +
                        "It is not that simple. People who seem to easily let go of a deep-rooted hatred are not trustworthy. After each physical examination following her return for a mission, GreyThroat repeatedly confirms her blood originium-crystal density, accompanied by a near-suffocating fear and subsequent relief, perhaps a reflection of the guilt and ensuing sorrow. GreyThroat still often wakes up with nightmares, and rarely has an opportunity to sleep, whether on a mission or in the Rhodes Island dormitories.\n" +
                        "GreyThroat is still her old self, not like Amiya, not like anyone else. But she will still fight for Rhodes Island, and nobody doubts this anymore.",
                "\"Scared? Of course. How could I not be scared?\"\n" +
                        "\"When my father let go of my hand and was swallowed up by the crowd, I never saw him again. At that time, I didn't dare look directly at my mother, " +
                        "and only felt the heat draining from her hand. I didn't understand what happened, why the patients we rescued turned into rioters overnight. Not until I saw my Infected friend, " +
                        "a greasy wrench in hand, vandalizing the street... like a lunatic. Even today, I sometimes still see her expression. What does that mean? I'm not sure.\"\n" +
                        "\"From then on, I couldn't stay calm when talking to Infected people. Since I probably don't need to hide anything from you, Doctor, I'll be frank. " +
                        "In the worst cases, I felt like vomiting. Amiya is the exception though. I really appreciate her. Oh, but don't tell her I said that.\"\n" +
                        "\"But, I've also seen the Infected across this world. The hungry woman using her own blood to nourish a ghastly, malnourished child, exiled to the scorched soil of the barrens, " +
                        "or admitted to some \"clinic\" in name only to die. With no treatment and no food, they eat the soil at the base of the walls, surrounded by the inorganic, " +
                        "petrified bodies of those whose ridiculous lives have long expired. And what is on the other side of that wall? A peaceful, prosperous mobile city devoid of hunger.\"\n" +
                        "\"I have seen the exiled Infected rescue travellers, and I have also witnessed travelling doctors who help these Infected crushed by terror. After becoming an Operator, " +
                        "I have also become involved, trying to help those who have not joined a mobile city-state avoid Catastrophes. One incident still is fresh on my mind. " +
                        "One time, in the face of a flood, Infected villagers held back the water with their own flesh, buying just enough time for the evacuation. " +
                        "And so those Infected gave their lives, their bodies now floating on top of the water, but nobody cares. Do you know how the villagers view these heroes? " +
                        "With disgust. Whether they hate these people or the Originium crystals on their skin, I cannot say.\"\n" +
                        "\"And why is this happening? Amiya told me before, because everyone is a living person. Everyone wants to live, and has the right to survive.\"\n" +
                        "\"Did you know? When Amiya said these words, she did not look like a young girl at all. But I am not the same as her, and there is inevitably a difference between the Infected and non-Infected. " +
                        "I just want to find the balance between the Infected and the people we were born to be, for those around me, for who I am inside. Nothing more, Doctor.\"\n\n" +
                        "--GreyThroat's conversation record with the Doctor after a certain medical examination. The recorder believes that this conversation will help the Infected understand GreyThroat, " +
                        "and with GreyThroat's tacit consent, this recording was added to her personal files.",
                5, "rhodesisland",
                "Maybe GreyThroat hasn't realized it yet, but in fact, everyone's impression towards her has changed recently. Though she still tries to avoid contact with the Infected most of the time, one Infected operator recalled an episode in which he had fallen into a trap on a mission due to his own error and was serious injured only to be immediately rescued by GreyThroat, who had been covering them from the jungle. Afterwards, the operator who was most known for her disgust towards the Infected was administering emergency treatments for him, her face contorted and arms trembling as she saved his life. He will never forget GreyThroat's expression as she fought to contain her all-too-visible trembling. At that time, he did not feel discriminated against, and suddenly came to understand GreyThroat.");
        opData.add(greythroat);
        OperatorInfo.Operator3Star fang = new OperatorInfo.Operator3Star("Fang", "vanguard",
                "Captain of Op Reserve Team A1. She has prior experience working with a Columbian garrison, and has proven herself to be outstanding in combat. After joining Rhodes Island, she had no problem adjusting to her current work. She joined the Operation Planning Group with its original members, Beagle and Kroos, and has worked hard to become its leading Operator.",
                "Imaging tests have shown the outlines of her internal organs to be indistinct due to abnormal shadows. Originium granules have been detected in her circulatory system, confirming her to be infected with Oripathy.\n" +
                        "\n" +
                        "[Cell-Originium Fusion Rate] 7.4%\n" +
                        "She presents no obvious external symptoms of Oripathy.\n" +
                        "\n" +
                        "[Blood Crystal Density] 0.2 u/L\n" +
                        "Degree of infection is minor and physical symptoms are not yet present.",
                "Fang shows off a Kuranta's natural dominance in battle by always holding the most advantageous position at the critical moment. To strike a balance between power and control, Fang prefers to use a spear, which she applies with versatility in any situation. Fang acts impulsively and springs into action before others even finish talking.",
                "Immigrants from many nations came to the outskirts of Columbia. Fang, Kroos, and Beagle all met there after moving from their respective homelands. After realizing their common goals, they joined the city guard and underwent basic training together. The humorless Fang, the gentle Beagle, and the laid-back Kroos possess wildly different personalities yet rely on one another for support.\n",
                "Before an election day, a Catastrophe struck the outskirts of Columbia. In their efforts to aid others in escaping, all three Operators were infected with Oripathy. Fang suffered discrimination due to her condition and fell into deep despair. Kroos rallied the three of them and together they went to Rhodes Island.",
                "After arriving at Rhodes Island, Fang and her two companions as well as Hibiscus and Lava were all trained by Dobermann. The training was extraordinarily intense, and Kroos and Hibiscus seized every opportunity they could to goof off. Even Fang as their captain couldn't do anything to control them. Currently, Fang is training herself to be as intense and strict as her teacher, Dobermann. However, if the giddy expression on her face as she eats fruit is any indication, she's got a long way to go.\n",
                3,
                "rhodesisland");
        opData.add(fang);

        OperatorInfo.Operator2Star yato = new OperatorInfo.Operator2Star("Yato", "vanguard",
                "Yato is the team leader of the core operators. She is a loyal and dependable warrior.",
                "Radiological scans showed blurred outlines of internal organs with visible abnormalities. Originium Particle Tests of the circulatory system indicate Oripathy. The subject is confirmed as Infected.\n" +
                        "\n" +
                        "[Cell-Originium Assimilation] 8%\n" +
                        "No major visible symptoms of infection.\n" +
                        "\n" +
                        "[Blood Originium-Crystal Density] 0.22u/L\n" +
                        "Severity of infection remains low, with no major hindrance to motor functions.",
                "A quiet warrior with no particularly memorable traits except for the mask she wears.\n" +
                        "As a result, people tend to overlook the fact that she's there. She doesn't seem to mind though, instead focusing on diligently carrying out every task given to her.\n" +
                        "Dr. Kal'tsit has praised her as \"the cornerstone of Rhodes Island.\"",
                "No one knows when Yato joined Rhodes Island, and no one has ever tried to find out.\n" +
                        "Her presence as a Rhodes Island Operator is taken for granted, but if she were to suddenly disappear one day, it's unclear how many people would actually notice.",
                "Some curious Operators once asked Yato about whether she had a history with Noir Corne, but she didn't provide a straightforward answer.\n" +
                        "Like Noir Corne, she is rarely seen without her mask.",
                "N/A",
                2,
                "rhodesisland");
        opData.add(yato);
    }
     */
