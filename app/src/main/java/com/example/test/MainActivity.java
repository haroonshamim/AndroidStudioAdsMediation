package com.example.test;

import com.applovin.mediation.AppLovinExtras;
import com.applovin.mediation.ApplovinAdapter;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.MaxReward;
import com.applovin.sdk.AppLovinPrivacySettings;



import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxRewardedAdListener;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.mediation.ads.MaxRewardedAd;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.AdapterStatus;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.*;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import android.widget.Toast;
import com.example.test.databinding.ActivityMainBinding;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.ironsource.mediationsdk.IronSource;

public class MainActivity extends AppCompatActivity implements MaxAdViewAdListener, MaxRewardedAdListener {
    // Remove the below line after defining your own ad unit ID.
    private static final String TOAST_TEXT = "Test ads are being shown. "
            + "To show live ads, replace the ad unit ID in res/values/strings.xml with your own ad unit ID.";
    private static final String TAG = "ADMOB MEDIATOR";

    private static final int START_LEVEL = 1;
    private int mLevel;


    private Button INIT_AdMobButton;
    private Button AdMobInterstetialButton;
    private Button AdMobBannerButton;
    private Button AdMobRewardedButton;


    private InterstitialAd GOOGLEADMEDIATOR_mInterstitialAd;
    private AdView GOOGLEADMEDIATOR_mBannerAd;
    private RewardedAd GOOGLEADMEDIATOR_mRewardedAd;


    private Button INIT_AppLovinButton;
    private Button AppLovinBannerButton;
    private Button AppLovinInterStetialButton;
    private Button AppLovinRewardedButton;

    private MaxAdView AppLovinBanner;
    private MaxInterstitialAd AppLovininterstitialAd;
    private int retryAttempt;
    private MaxRewardedAd AppLovinrewardedAd;







    //private Button AppLovinRewardedButton;








    private TextView mLevelTextView;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        INIT_AdMobButton=binding.INITADMOB;
        AdMobBannerButton=binding.AdMobBanner;
        AdMobRewardedButton=binding.AdMobRewarded;
        AdMobInterstetialButton = binding.AdMobInterstetial;

        INIT_AppLovinButton=binding.INITAPPLOVIN;
        AppLovinBannerButton=binding.AppLovinBanner;
        AppLovinInterStetialButton=binding.AppLovinInterstetial;
        AppLovinRewardedButton=binding.AppLovinRewarded;



        // Load the InterstitialAd and set the adUnitId (defined in values/strings.xml).
         // Create the next level button, which tries to show an interstitial when clicked.


        AdMobInterstetialButton.setEnabled(false);
        AdMobBannerButton.setEnabled(false);
        AdMobRewardedButton.setEnabled(false);
        AppLovinBannerButton.setEnabled(false);
        AppLovinInterStetialButton.setEnabled(false);
        AppLovinRewardedButton.setEnabled(false);


        //ADMOB BUTTONS CALLING

        INIT_AdMobButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InitAdMobMediator();
            }
        });
        AdMobInterstetialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GOOGLEADMEDIATOR_showInterstitial();
            }
        });
        AdMobBannerButton = binding.AdMobBanner;
        AdMobBannerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GOOGLEADMEDIATOR_showBanner();
            }
        });
        AdMobRewardedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GOOGLEADMEDIATOR_showRewardedAd();
            }
        });


        //APPLOVIN BUTTONS CALLING

        INIT_AppLovinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InitAppLovinMediator();
            }
        });
        AppLovinBannerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                APPLOVIN_showBanner();
            }
        });
        AppLovinInterStetialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                APPLOVIN_showInterStetialAd();
            }
        });
        AppLovinRewardedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                APPLOVIN_showRewardedAd();
            }
        });


        // Create the text view to show the level number.
        mLevelTextView = binding.level;
        mLevel = START_LEVEL;

        // Toasts the test ad message on the screen. Remove this after defining your own ad unit ID.
        Toast.makeText(this, TOAST_TEXT, Toast.LENGTH_LONG).show();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //GOOGLE ADMEDIATORS
    public void InitAdMobMediator()
    {        Context context = this;
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

                Map<String, AdapterStatus> statusMap = initializationStatus.getAdapterStatusMap();
                for (String adapterClass : statusMap.keySet()) {
                    AdapterStatus status = statusMap.get(adapterClass);
                    Log.d("MyApp", String.format(
                            "Adapter name: %s, Description: %s, Latency: %d",
                            adapterClass, status.getDescription(), status.getLatency()));



                    AppLovinPrivacySettings.setHasUserConsent(true, context);
                    AppLovinPrivacySettings.setIsAgeRestrictedUser(true, context);
                    AppLovinPrivacySettings.setDoNotSell(true, context);
                    IronSource.setMetaData("do_not_sell", "true");


                    IronSource.setConsent(true);


                    Bundle extras = new AppLovinExtras.Builder()
                            .setMuteAudio(true)
                            .build();
                    AdRequest request = new AdRequest.Builder()
                            .addNetworkExtrasBundle(ApplovinAdapter.class, extras)
                            .build();

                    AppLovinPrivacySettings.setHasUserConsent( true, context );
                    AppLovinPrivacySettings.setIsAgeRestrictedUser( true, context );
                    AppLovinPrivacySettings.setDoNotSell( true, context );

                    AdMobInterstetialButton.setEnabled(true);
                    AdMobBannerButton.setEnabled(true);
                    AdMobRewardedButton.setEnabled(true);



                    GOOGLEADMEDIATOR_loadInterstitialAd();
                    GOOGLEADMEDIATOR_loadBannerAd();
                    GOOGLEADMEDIATOR_loadRewardedAd();

                }

            }
        });
    }
    public void GOOGLEADMEDIATOR_loadInterstitialAd() {



        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this, getString(R.string.admob_interstitial_ad_unit_id), adRequest,
                new InterstitialAdLoadCallback() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onAdLoaded( InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        GOOGLEADMEDIATOR_mInterstitialAd = interstitialAd;
                        AdMobInterstetialButton.setEnabled(true);

                        Log.d("LOADED name: ","ADMOBMEDIATOR:"+ GOOGLEADMEDIATOR_mInterstitialAd.getResponseInfo().getMediationAdapterClassName());
                        Toast.makeText(MainActivity.this, "onAdLoaded()", Toast.LENGTH_SHORT).show();
                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdClicked() {
                                        // Called when a click is recorded for an ad.
                                        Log.d(TAG, "ADMOBMEDIATOR:Ad was clicked.");
                                    }

                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when ad is dismissed.
                                        // Set the ad reference to null so you don't show the ad a second time.
                                        Log.d(TAG, "ADMOBMEDIATOR:Ad dismissed fullscreen content.");
                                        GOOGLEADMEDIATOR_mInterstitialAd = null;
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        // Called when ad fails to show.
                                        Log.e(TAG, "ADMOBMEDIATOR:Ad failed to show fullscreen content.");
                                        GOOGLEADMEDIATOR_mInterstitialAd = null;
                                    }

                                    @Override
                                    public void onAdImpression() {
                                        // Called when an impression is recorded for an ad.
                                        Log.d(TAG, "ADMOBMEDIATOR:Ad recorded an impression.");
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        // Called when ad is shown.
                                        Log.d(TAG, "ADMOBMEDIATOR:Ad showed fullscreen content.");
                                    }

                                });
                    }

                    @Override
                    public void onAdFailedToLoad( LoadAdError loadAdError) {
                        // Handle the error
                        Log.i(TAG, loadAdError.getMessage());
                        GOOGLEADMEDIATOR_mInterstitialAd = null;
                        AdMobInterstetialButton.setEnabled(false);

                        String error = String.format(
                                Locale.ENGLISH,
                                "domain: %s, code: %d, message: %s",
                                loadAdError.getDomain(),
                                loadAdError.getCode(),
                                loadAdError.getMessage());
                        Toast.makeText(
                                MainActivity.this,
                                "onAdFailedToLoad() with error: " + error, Toast.LENGTH_SHORT)
                                .show();
                    }
                });
    }
    private void GOOGLEADMEDIATOR_showInterstitial() {
        // Show the ad if it"s ready. Otherwise toast and reload the ad.
        if (GOOGLEADMEDIATOR_mInterstitialAd != null) {

            Log.d("LOADED name: ","ADMOBMEDIATOR_IAD:"+ GOOGLEADMEDIATOR_mInterstitialAd.getResponseInfo().getMediationAdapterClassName());

            GOOGLEADMEDIATOR_mInterstitialAd.show(this);
        } else {
            Toast.makeText(this, "ADMOBMEDIATOR:Ad did not load", Toast.LENGTH_SHORT).show();
            goToNextLevel();
        }
    }
    private void GOOGLEADMEDIATOR_showBanner() {

        // Show the ad if it"s ready. Otherwise toast and reload the ad.
        if (GOOGLEADMEDIATOR_mBannerAd == null) {

            Log.d("LOADED name: ","ADMOBMEDIATOR_BANNER:"+ GOOGLEADMEDIATOR_mBannerAd.getResponseInfo().getMediationAdapterClassName());


            GOOGLEADMEDIATOR_loadBannerAd();
        } else {
            Toast.makeText(this, "ADMOBMEDIATOR:Banner Ad Call Sent", Toast.LENGTH_SHORT).show();
        }
    }
    public void GOOGLEADMEDIATOR_loadBannerAd() {

        GOOGLEADMEDIATOR_mBannerAd =findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder().build();
        GOOGLEADMEDIATOR_mBannerAd.loadAd(adRequest);
        GOOGLEADMEDIATOR_mBannerAd.setAdListener(new AdListener() {
            @Override
            public void onAdClicked() {
                Log.d(TAG, "ADMOBMEDIATOR:Banner Ad Has Been Clicked");
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
                Log.d(TAG, "ADMOBMEDIATOR:Banner Ad Has Been Closed");
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                Log.d(TAG, "ADMOBMEDIATOR:Banner Ad Has Failed To Load");
            }

            @Override
            public void onAdImpression() {
                Log.d(TAG, "ADMOBMEDIATOR:On Ad Impression Has Been Called");
            }

            @Override
            public void onAdLoaded() {
                Log.d(TAG, "ADMOBMEDIATOR:Banner Ad Has been Loaded");
            }

            @Override
            public void onAdOpened() {
                Log.d(TAG, "ADMOBMEDIATOR:Banner Ad Has been Opened");
            }
        });

    }
    private void GOOGLEADMEDIATOR_showRewardedAd() {

        if ( GOOGLEADMEDIATOR_mRewardedAd != null)
        {
            Log.d("LOADED name: ","ADMOBMEDIATOR_REWARDED:"+ GOOGLEADMEDIATOR_mRewardedAd.getResponseInfo().getMediationAdapterClassName());


            // Show the ad if it"s ready. Otherwise toast and reload the ad.
            Activity activityContext = MainActivity.this;
            GOOGLEADMEDIATOR_mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
            @Override
            public void onUserEarnedReward(RewardItem rewardItem)
            {

                Log.d(TAG, "ADMOBMEDIATOR:USERHASBEENREWARDEd");
            }



        });
    }
    else

    {
        Log.d(TAG, "ADMOBMEDIATOR:The rewarded ad wasn't ready yet.");
    }

    }
    public void GOOGLEADMEDIATOR_loadRewardedAd() {




        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(this, getString(R.string.admob_rewarded_ad_unit_id),
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad( LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d(TAG, loadAdError.toString());
                        GOOGLEADMEDIATOR_mRewardedAd = null;

                        Log.d(TAG, "ADMOBMEDIATOR:Rewarded Ad Failed to load");
                    }

                    @Override
                    public void onAdLoaded( RewardedAd rewardedAd) {
                        GOOGLEADMEDIATOR_mRewardedAd = rewardedAd;
                        Log.d(TAG, "ADMOBMEDIATOR:Rewarded Ad was loaded.");
                    }


                });
/*
       GOOGLEADMEDIATOR_mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdClicked() {
                // Called when a click is recorded for an ad.
                Log.d(TAG, "ADMOBMEDIATOR:Ad was clicked.");
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                // Set the ad reference to null so you don't show the ad a second time.
                Log.d(TAG, "ADMOBMEDIATOR:Ad dismissed fullscreen content.");
                GOOGLEADMEDIATOR_mRewardedAd = null;
            }

            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {
                // Called when ad fails to show.
                Log.e(TAG, "ADMOBMEDIATOR:Ad failed to show fullscreen content.");
                GOOGLEADMEDIATOR_mRewardedAd = null;
            }

            @Override
            public void onAdImpression() {
                // Called when an impression is recorded for an ad.
                Log.d(TAG, "ADMOBMEDIATOR:Ad recorded an impression.");
            }

            @Override
            public void onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d(TAG, "ADMOBMEDIATOR:Ad showed fullscreen content.");
            }
        });
*/
    }


    //APPLOVIN MEDIATORS

    public void InitAppLovinMediator()
    {
        Context context = this;

        AppLovinSdk.getInstance( context ).setMediationProvider( "max" );
        AppLovinSdk.initializeSdk( context, new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(AppLovinSdkConfiguration config) {

              //  AppLovinBannerButton.setEnabled(true);
                AppLovinInterStetialButton.setEnabled(true);
                AppLovinRewardedButton.setEnabled(true);



               // APPLOVIN_loadBannerAd();
                APPLOVIN_loadInterStetialAd();
                APPLOVIN_loadRewardedAd();
            }


        } );
    }
    public void APPLOVIN_loadBannerAd() {
        AppLovinBanner = new MaxAdView(  getString(R.string.applovin_banner_ad_unit_id), this );

        AppLovinBanner.setListener(this);
        // Stretch to the width of the screen for banners to be fully functional
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        // Banner height on phones and tablets is 50 and 90, respectively
        //int heightPx = getResources().getDimensionPixelSize( R.dimen.banner_height );
        int heightPx =50;
        AppLovinBanner.setLayoutParams( new FrameLayout.LayoutParams( width, heightPx ) );
        ViewGroup rootView = findViewById( android.R.id.content );
        rootView.addView( AppLovinBanner);
        // Load the ad
        AppLovinBanner.loadAd();

    }
    public void APPLOVIN_showBanner()
    {
        APPLOVIN_loadBannerAd();
            /*
        }
        else
        {
            APPLOVIN_loadBannerAd();
            Toast.makeText(this, "APPLOVINMEDIATOR:Banner Ad Call Sent", Toast.LENGTH_SHORT).show();
        }

             */
    }
    public void APPLOVIN_loadRewardedAd()
    {
        AppLovinrewardedAd = MaxRewardedAd.getInstance( getString(R.string.applovin_rewarded_ad_unit_id), this );
        AppLovinrewardedAd.setListener(this);
        AppLovinrewardedAd.loadAd();
    }
    public void APPLOVIN_showRewardedAd()
    {
        if ( AppLovinrewardedAd.isReady() )
        {
            AppLovinrewardedAd.showAd();
        }
        else
        {
            Toast.makeText(this, "APPLOVINMEDIATOR:REWARDED Ad Call Sent", Toast.LENGTH_SHORT).show();
            APPLOVIN_loadRewardedAd();
        }

    }
    public void APPLOVIN_loadInterStetialAd() {


        AppLovininterstitialAd= new MaxInterstitialAd( getString(R.string.applovin_interstitial_ad_unit_id), this );
        AppLovininterstitialAd.setListener(this);

        // Load the first ad
        AppLovininterstitialAd.loadAd();

    }
    public void APPLOVIN_showInterStetialAd()
    {
        if ( AppLovininterstitialAd.isReady() )
        {
            AppLovininterstitialAd.showAd();
        }
        else
        {   Toast.makeText(this, "APPLOVINMEDIATOR:INTERSTETIAL Ad Call Sent", Toast.LENGTH_SHORT).show();
            APPLOVIN_loadInterStetialAd();
        }

    }



    private void goToNextLevel() {
        // Show the next level and reload the ad to prepare for the level after.
     //   mLevelTextView.setText("Level " + (++mLevel));
        if (GOOGLEADMEDIATOR_mInterstitialAd == null) {

            GOOGLEADMEDIATOR_loadInterstitialAd();
        }
    }

    @Override
    public void onAdExpanded(MaxAd ad) {

    }

    @Override
    public void onAdCollapsed(MaxAd ad) {

    }

    @Override
    public void onAdLoaded(MaxAd ad) {

        Log.d("LOADED name: ","APPLOVINMEDIATOR:"+ ad.getNetworkName()+" AND TYPE:"+ad.getDspName());

    }

    @Override
    public void onAdDisplayed(MaxAd ad) {

    }

    @Override
    public void onAdHidden(MaxAd ad) {

    }

    @Override
    public void onAdClicked(MaxAd ad) {

    }

    @Override
    public void onAdLoadFailed(String adUnitId, MaxError error) {

        Log.d("FAILED name: ","APPLOVINMEDIATOR:"+ error.getMessage());

        retryAttempt++;
        long delayMillis = TimeUnit.SECONDS.toMillis( (long) Math.pow( 2, Math.min( 6, retryAttempt ) ) );

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {

                AppLovininterstitialAd.loadAd();
            }
        }, delayMillis );

    }

    @Override
    public void onAdDisplayFailed(MaxAd ad, MaxError error) {

    }

    @Override
    public void onUserRewarded(MaxAd ad, MaxReward reward) {

    }

    @Override
    public void onRewardedVideoStarted(MaxAd ad) {

    }

    @Override
    public void onRewardedVideoCompleted(MaxAd ad) {

    }



    @Override
    public void onResume() {
        super.onResume();
        IronSource.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        IronSource.onPause(this);
    }









}