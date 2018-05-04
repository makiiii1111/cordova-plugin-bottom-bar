package bottombar;

import android.view.View;

import com.busll.quzhouxing.BottomBarActivity;

import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONException;


/**
 * This class echoes a string called from JavaScript.
 */
public class BottomBar extends CordovaPlugin{
    public String webViewUrl = "https://youliao.163yun.com/h5/list/?appkey=68cfb2de79e34e918b44bbd9036b5976&secretkey=0484a24f309442ceb1e9003f1b6d707d&s=semi";
    @Override
    public boolean execute(String action, final CordovaArgs args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("hideBottomBar")) {
          cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
              hideBottomBar();
            }
          });
        }else if(action.equals("showBottomBar")){
          cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
              showBottomBar();
            }
          });
        } else if(action.equals("setUrl")){
            webViewUrl = args.getString(0);
        }
          else {
          return false;
        }
      return true;
    }
  public void showBottomBar(){
    if(BottomBarActivity.bottomBar.getVisibility()== View.GONE) {
      BottomBarActivity.bottomBar.setVisibility(View.VISIBLE);
    }
  }
  public void hideBottomBar() {
    if (BottomBarActivity.bottomBar.getVisibility() == View.VISIBLE) {
      BottomBarActivity.bottomBar.setVisibility(View.GONE);
    }
  }
}
