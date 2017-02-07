package mm.widgetm;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class AppWidget extends AppWidgetProvider {
    static  boolean notRevealed=true;
    static MediaPlayer mediaPlayer;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);
        //web
        Intent intentWEB=new Intent();
        intentWEB.setAction("web");
        PendingIntent piWeb=PendingIntent.getBroadcast(context,0,intentWEB,0);
        views.setOnClickPendingIntent(R.id.button1,piWeb);
        //image
        Intent intentImage=new Intent();
        intentImage.setAction("image");
        intentImage.putExtra("imageViewID",appWidgetId);
        PendingIntent piImage=PendingIntent.getBroadcast(context,2,intentImage,0);
        views.setOnClickPendingIntent(R.id.button2,piImage);
        //audio
        Intent intentAudio=new Intent();
        intentAudio.setAction("audio");
        PendingIntent piAudio=PendingIntent.getBroadcast(context,1,intentAudio,0);
        views.setOnClickPendingIntent(R.id.button3,piAudio);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (mediaPlayer==null)mediaPlayer=MediaPlayer.create(context, R.raw.mambo);
        switch (intent.getAction()){
            case ("web"):
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.pja.edu.pl"));
                browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(browserIntent);
                break;
            case ("image"):
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);
                int id = intent.getIntExtra("imageViewID", -1);
                if (notRevealed){
                    views.setImageViewResource(R.id.imageView, R.drawable.kot_burak);
                }else{
                    views.setImageViewResource(R.id.imageView, R.drawable.kot);
                }
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                appWidgetManager.updateAppWidget(id, views);
                notRevealed=!notRevealed;
                break;
            case ("audio"):
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                }else{
                    mediaPlayer.start();
                }
                break;
        }
    }
}

