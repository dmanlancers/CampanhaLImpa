package com.transparencia.campanha_limpa.formularios;


import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class CustomOnItemSelectedListener implements OnItemSelectedListener {
	
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		parent.getItemAtPosition(pos);
		if(pos == 1){
			Toast.makeText(parent.getContext(), 
				"OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
				Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

}