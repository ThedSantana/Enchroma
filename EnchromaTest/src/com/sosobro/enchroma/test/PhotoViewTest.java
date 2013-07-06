package com.sosobro.enchroma.test;

import java.io.File;

import com.sosobro.enchroma.*;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcel;
import android.test.ActivityUnitTestCase;

public class PhotoViewTest extends ActivityUnitTestCase<MainActivity> {
	
	private PhotoView _photoView;
	
	public PhotoViewTest( ) {
		super( MainActivity.class );
	}

	protected void setUp( ) throws Exception {
		super.setUp();
		
		FileUtils.instance = new Shims.FileUtilsShim();
		ThumbnailBuilder.instance = new Shims.ThumbnailBuilderShim();
		
		MainActivity a = (MainActivity) startActivity(
			new Intent( getInstrumentation().getTargetContext(), MainActivity.class ),
			null, null );
		
		_photoView = (PhotoView) a.findViewById( R.id.subjectImg );
	}

	public void testActivityReturns( ) {
		_photoView.onActivityResult( Activity.RESULT_OK, null );
		
		// Did we create the thumb and save it to the state?
		PhotoView.SavedState ss = (PhotoView.SavedState) _photoView.onSaveInstanceState();
		assertNotNull( ss.thumb );
		
		// When the state is restored we set the image.
		_photoView.onRestoreInstanceState( ss );
		assertEquals( 8, _photoView.getDrawable().getIntrinsicWidth() );
	}
	
	public void testSaveRestoreState( ) {
		Parcel p = Parcel.obtain();
		
		// Save
		PhotoView.SavedState ss = new PhotoView.SavedState();
		ss.photoFile = new File( Shims.FileUtilsShim.SubjectFilePath );
		ss.thumb = ThumbnailBuilder.instance.createThumbnail( ss.photoFile );
		ss.writeToParcel( p, 0 );

		// Restore
		p.setDataPosition( 0 );
		ss = new PhotoView.SavedState( p );
		assertEquals( Shims.FileUtilsShim.SubjectFilePath, ss.photoFile.toString() );
		assertNotNull( ss.thumb );
		
		p.recycle();
	}
	
	public void testSaveRestoreEmptyState( ) {
		Parcel p = Parcel.obtain();
		
		// Save
		PhotoView.SavedState ss = new PhotoView.SavedState();
		ss.writeToParcel( p, 0 );

		// Restore
		p.setDataPosition( 0 );
		ss = new PhotoView.SavedState( p );
		assertNull( ss.photoFile );
		assertNull( ss.thumb );
		
		p.recycle();
	}
}