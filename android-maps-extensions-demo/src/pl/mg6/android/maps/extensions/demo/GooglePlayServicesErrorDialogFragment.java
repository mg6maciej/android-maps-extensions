/*
 * Copyright (C) 2013 Maciej Górski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.mg6.android.maps.extensions.demo;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class GooglePlayServicesErrorDialogFragment extends DialogFragment {

	private static final String TAG = GooglePlayServicesErrorDialogFragment.class.getSimpleName();

	private static final String KEY_STATUS = "status";

	public static GooglePlayServicesErrorDialogFragment newInstance(int status) {
		GooglePlayServicesErrorDialogFragment f = new GooglePlayServicesErrorDialogFragment();
		Bundle args = new Bundle();
		args.putInt(KEY_STATUS, status);
		f.setArguments(args);
		return f;
	}

	public static void showDialog(int status, FragmentActivity activity) {
		FragmentManager fm = activity.getSupportFragmentManager();
		GooglePlayServicesErrorDialogFragment f = newInstance(status);
		f.show(fm, TAG);
	}

	public static boolean showDialogIfNotAvailable(FragmentActivity activity) {
		removeDialog(activity);
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
		boolean available = status == ConnectionResult.SUCCESS;
		if (!available) {
			if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
				showDialog(status, activity);
			} else {
				Toast.makeText(activity.getApplication(), "Google Play services not available", Toast.LENGTH_SHORT).show();
			}
		}
		return available;
	}

	public static void removeDialog(FragmentActivity activity) {
		FragmentManager fm = activity.getSupportFragmentManager();
		Fragment f = fm.findFragmentByTag(TAG);
		if (f != null) {
			fm.beginTransaction().remove(f).commit();
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Bundle args = getArguments();
		int status = args.getInt(KEY_STATUS);
		return GooglePlayServicesUtil.getErrorDialog(status, getActivity(), 0);
	}
}
