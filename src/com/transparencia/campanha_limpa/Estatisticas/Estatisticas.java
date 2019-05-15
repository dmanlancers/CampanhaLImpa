package com.transparencia.campanha_limpa.Estatisticas;

import com.transparencia.campanha_limpa.R;
import com.transparencia.campanha_limpa.CampanhaLimpa;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Estatisticas extends Activity {

	private WebView webView;
	private ProgressDialog mProgress;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.charts);

		webView = (WebView) findViewById(R.id.webView1);
		
		mProgress = ProgressDialog.show(this, "A carregar...", "Aguarde...");
		// add a WebViewClient for WebView, which actually handles loading data from web
		webView.setWebViewClient(new WebViewClient() {
			// load url
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			// when finish loading page
			public void onPageFinished(WebView view, String url) {
				if(mProgress.isShowing()) {
					mProgress.dismiss();
				}
			}
		});
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl("http://campanhalimpa.transparencia.pt/stat/cartaz");
	}








	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			Intent home = new Intent(this, CampanhaLimpa.class);
			home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(home);

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
