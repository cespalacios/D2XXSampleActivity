package com.ftdi.d2xx;


import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ListadeContactos extends ListActivity {
	/** Called when the activity is first created. */
	String phone;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		setTitle("Lista de Contactos");

		// Query: contacts with phone shorted by name
		Cursor mCursor = getContentResolver().query(
				Data.CONTENT_URI,
				new String[] { Data._ID, Data.DISPLAY_NAME, Phone.NUMBER,
						Phone.TYPE },
				Data.MIMETYPE + "='" + Phone.CONTENT_ITEM_TYPE + "' AND "
						+ Phone.NUMBER + " IS NOT NULL", null,
				Data.DISPLAY_NAME + " ASC");

		startManagingCursor(mCursor);

		// Setup the list
		ListAdapter adapter = new SimpleCursorAdapter(this, // context
				android.R.layout.simple_list_item_2, // Layout for the rows
				mCursor, // cursor
				new String[] { Data.DISPLAY_NAME, Phone.NUMBER }, // cursor
																	// fields
				new int[] { android.R.id.text1, android.R.id.text2 } // view
																		// fields
		);
		setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent result = new Intent();

		// Get the data
		Cursor c = (Cursor) getListAdapter().getItem(position);
		int colIdx = c.getColumnIndex(Phone.NUMBER);
		phone = c.getString(colIdx);

		// Save the phone to return it to the caller
		result.putExtra("phone", phone);
		setResult(Activity.RESULT_OK, result);

		// Close this activity (return to caller)
		call();
		finish();
	}
	
	private void call() {
		try {
			Intent callIntent = new Intent(Intent.ACTION_CALL);
			callIntent.setData(Uri.parse("tel:" + phone.toString().trim()));
			startActivity(callIntent);
		} catch (ActivityNotFoundException activityException) {
			Log.e("helloandroid dialing example", "Call failed",
					activityException);
		}
	}
	
	
}

