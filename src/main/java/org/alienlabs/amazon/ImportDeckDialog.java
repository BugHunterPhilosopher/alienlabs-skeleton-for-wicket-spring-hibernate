package org.alienlabs.amazon;

import java.io.UnsupportedEncodingException;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.lang.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;

@edu.umd.cs.findbugs.annotations.SuppressFBWarnings(value = { "SE_INNER_CLASS",
"SIC_INNER_SHOULD_BE_STATIC_ANON" }, justification = "In Wicket, serializable inner classes are common. And as the parent Page is serialized as well, this is no concern. This is no bad practice in Wicket")
public class ImportDeckDialog extends Panel
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ImportDeckDialog.class);
	private static final long serialVersionUID = 1L;
	final FileUploadField file;

	public ImportDeckDialog(final String id)
	{
		super(id);
		Injector.get().inject(this);

		final Form<Void> form = new Form<>("form");
		this.file = new FileUploadField("deckFile");
		form.setMarkupId("inputForm").setOutputMarkupId(true);
		form.setMaxSize(Bytes.kilobytes(5));
		form.setMultiPart(true);
		form.add(this.file);
		this.add(form);

		final Button upload = new Button("upload")
		{
			@Override
			public void onSubmit()
			{
				ImportDeckDialog.LOGGER.info("trying to upload something");

				//final FileUpload fupload = ImportDeckDialog.this.file.getFileUpload();
				//if (fupload == null)
				//{
					// No file was provided
				//	ImportDeckDialog.LOGGER.info("Please provide a valid file");
				//	return;
				//}
				//else if (fupload.getSize() == 0)
				//{
				//	ImportDeckDialog.LOGGER.info("Please provide a non-empty file");
				//	return;
				//}
				//else if ((fupload.getClientFileName() == null)
				//		|| ("".equals(fupload.getClientFileName().trim()))
				//		|| (fupload.getClientFileName().endsWith(".txt")))
				//{
				//	ImportDeckDialog.LOGGER.info("Please provide a valid file");
				//	return;
				//}

				//ImportDeckDialog.LOGGER.info("uploading file: "
				//		+ ImportDeckDialog.this.file.getFileUpload().getClientFileName());

				//try
				//{
				//	new String(fupload.getBytes(), "UTF-8");
					ImportDeckDialog.convert("/home/nostromo/test.avi", "/home/nostromo/test.mp3");
				//}
				//catch (final UnsupportedEncodingException e)
				//{
				//	ImportDeckDialog.LOGGER.info("Please provide a file encoded with UTF-8 charset");
				//	return;
				//}

				//ImportDeckDialog.LOGGER.info("successfully added deck: "
				//		+ fupload.getClientFileName());
				ImportDeckDialog.LOGGER.info("Your file has been successfully uploaded");
			}
		};
		
		form.add(upload);
	}
	
	public static void convert(String from, final String to) { 
		IMediaReader reader = ToolFactory.makeReader(from); 
		IMediaWriter writer = ToolFactory.makeWriter(to, reader); 
		int sampleRate = 44100; 
		int channels = 1; 
		writer.setMaskLateStreamExceptions(true); 
		writer.addAudioStream(1, 0, ICodec.ID.CODEC_ID_MP3, channels, sampleRate); 
		reader.addListener(writer); 
		while(reader.readPacket() == null);
	}
}
