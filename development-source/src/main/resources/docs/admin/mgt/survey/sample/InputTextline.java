package edu.vt.ward.survey;


import com.whomade.kycarrots.mgt.survey.vo.TbQuestion;
import com.whomade.kycarrots.mgt.survey.vo.TbResults;
import com.whomade.kycarrots.mgt.survey.vo.TbSurvey;
import jakarta.servlet.http.HttpServletRequest;

public class InputTextline extends Question {
  private static int minSize = 1;
  private static int maxSize = 100;
  private static int minMaxLength = 1;
  private static int maxMaxLength = 300;
  private int size = 30;
  private int maxLength = 300;
  private String label = "";
  private String value = ""; // contains the text the user inputs
  private long count = 0;
  private String SUMMARY_CD;

  public String getSUMMARY_CD() {
	return SUMMARY_CD;
}

public void setSUMMARY_CD(String sUMMARY_CD) {
	SUMMARY_CD = sUMMARY_CD;
}

public InputTextline (String surveyId) {
    super (surveyId);
  }

  public InputTextline (String surveyId, String text, String label ) {
    super (surveyId, text );
    this.setLabel ( label );
  }

  public InputTextline (String surveyId, TbQuestion tbQuestion ) {
    super (surveyId, tbQuestion ); // assign questionText
 
    try { this.setValue ( tbQuestion.getQUESTIONTEXT() ); }
    catch ( Exception e ) { }

    try { this.setLabel ( tbQuestion.getQUESTION_LABEL()); }
    catch ( Exception e ) { }

    try { this.setSUMMARY_CD ( tbQuestion.getSUMMARY_CD()); }
    catch ( Exception e ) { }

    try { this.setSize ( Integer.parseInt (tbQuestion.getTEXTSIZE()) ); }
    catch ( Exception e ) { }

    try { this.setMaxLength ( Integer.parseInt ( tbQuestion.getMAXLENGTH() ) ); }
    catch ( Exception e ) { }
    try { this.setCount (tbQuestion.getRESULT_CNT() ); }
    catch ( Exception e ) { }
  }

  // outputs text e.g. "Multiple choice - pick one" or "Short answer - one line"
  public String getQuestionTypeTextHTML () {
    return "Short Answer - one line";
  }

  public String getValue () {
    return this.value;
  }

  public void setValue ( String value ) {
    this.value = value;
  }

  public int getSize () {
    return this.size;
  }

  public void setSize ( int size ) {
    this.size = size;
  }

  public int getMaxLength () {
    return this.maxLength;
  }

  public void setMaxLength ( int maxLength ) {
    this.maxLength = maxLength;
  }

  public String getLabel () {
    return this.label;
  }

  public void setLabel ( String newLabel ) {
    this.label = newLabel;
  }

  public long getCount () {
    return this.count;
  }

  public void setCount ( long count ) {
	  this.count = count;
  }

  public void incrementCount () {
    this.count++;
  }
  public String getHTML_New ( TbSurvey survey, int mode, int sectionNr, int questionNr ) {
    StringBuffer s = new StringBuffer ();
	if ( mode == iSurveyEntryForm.modeEntry || mode == iSurveyEntryForm.modePreview ){
	    s.append ("<dd>");
	    s.append ("<div class=\"control-group\">");
	    s.append ("<label class=\"control-label\" for=\"form-field-1\">"+this.getText()+"</label>");
	    s.append ("<div class='controls'>"+this.getLabel ());
	    s.append ( "<input type=\"text\" name=\"q_" + sectionNr + "_" + questionNr + "\" value=\"\"" );
	    if ( this.getSize () > 0 )
	      s.append ( " size=\"" + String.valueOf ( this.getSize () ) + "\"" );
	    if ( this.getMaxLength () > 0 )
	      s.append ( " maxLength=\"" + String.valueOf ( this.getMaxLength () ) + "\"" );
	    if ( "2".equals(this.getSummaryCd()))
	      s.append ("numberOnly \"" );

	    s.append ( ">");
		s.append ("</div>");
		s.append ("</div>");
		s.append ("</dd>");
	}
	
	if( mode == iSurveyEntryForm.modeEdit || mode == iSurveyEntryForm.modeResultsExport){
	    s.append ("<div class=\"control-group\">");
	    s.append ("<label class=\"control-label\" for=\"form-field-1\">"+this.getText()+"</label>");
	    s.append ("<div class='controls'>"+this.getLabel ());
	    s.append ( "<input type=\"text\" class=\"input-206px\" name=\"q_" + sectionNr + "_" + questionNr + "\" value=\"\"" );
	    if ( this.getSize () > 0 )
	      s.append ( " size=\"" + String.valueOf ( this.getSize () ) + "\"" );
	    if ( this.getMaxLength () > 0 )
		      s.append ( " maxLength=\"" + String.valueOf ( this.getMaxLength () ) + "\"" );
	    if ( "2".equals(this.getSummaryCd()))
		      s.append ("numberOnly \"" );
	    s.append ( ">");
		s.append ("</div>");
		s.append ("</div>");
		
		s.append ("<script language=\"JavaScript\" type=\"text/javascript\"><!--\n");
		s.append ("document.form.q_" + sectionNr + "_" + questionNr + ".disabled = true;\n" );
		s.append ("//-->\n</script>\n");
		
	}
    
	if ( mode == iSurveyEntryForm.modeResultsSummary ) {
    	if ( survey.singleEntry ) {
        if ( this.getCount() > 0 ) {
          s.append ( "<span class=\"highlightresponses\">" + this.getValue () + "</span>" );
        }
        else {
          s.append ( "<span class=\"highlightresponses\"><i>"+"no answer"+"</i></span>" );
        }
      }
      else {
        s.append ( "<span class=\"highlightresponses\"><b>" );
        s.append ( String.valueOf ( this.getCount () ) + " ");
        if ( this.getCount() == 1 ) { s.append ( "response" ); } else { s.append ( "responses" ); }
        s.append ( "</b></span>" );
        if ( this.getCount() > 0 ) {
          s.append (" &nbsp;&nbsp;<a href=\"/mgt/survey/viewResultsQuestion.do?surveyId=" + this.surveyId + "&section=" + sectionNr + "&question=" + questionNr + "\">"+"responses"+"</a>&nbsp; <a href=\"/mgt/survey/viewResultsDetails.do?surveyId=" + this.surveyId + "&section=" + sectionNr + "&question=" + questionNr + "\">"+"모든 질문보기"+"</a>");
        }
      } // end: else: if ( survey.singleEntry )
    } // end: if ( mode == SurveyEntryForm.modeResultsSummary )

	if ( mode == iSurveyEntryForm.modeMod){
	    s.append ("<dd>");
	    s.append ("<div class=\"control-group\">");
	    s.append ("<label class=\"control-label\" for=\"form-field-1\">"+this.getText()+"</label>");
	    s.append ("<div class='controls'>"+this.getLabel ());
	    s.append ( "<input type=\"text\" name=\"q_" + sectionNr + "_" + questionNr + "\"" );
	    if ( this.getSize () > 0 )
	      s.append ( " size=\"" + String.valueOf ( this.getSize () ) + "\"" );
	    if ( this.getMaxLength () > 0 )
	      s.append ( " maxLength=\"" + String.valueOf ( this.getMaxLength () ) + "\"" );
	      
	    s.append ( " value=\"" + this.getValue() + "\"" );
	    
	    s.append ( ">");
		s.append ("</div>");
		s.append ("</div>");
		s.append ("</dd>");
	}

	return s.toString();
  }
  


  public String getEditFormHTML_New ( int sectionNr, int questionNr, String type,String above,String inputErrorId ) {
	    StringBuffer s = new StringBuffer ();

	    s.append ( "<h2>" + this.getQuestionTypeTextHTML () + "</h2>\n" );
	    s.append ( "Hint: You can leave Question prompt empty to make a short answer field; seem to belong to the previous question, e.g. first name, last name." );

	    s.append ( super.getEditFormBeginHTML_New ( sectionNr, questionNr,type,above ) );

	    if ( inputErrorId != null && inputErrorId.equals ( "invalidLabel" ) )
	      s.append ( "<br><font color=\"#ff0000\"><b>"+"Error!"+"</b></font> "+"Please enter a valid label."+"<br><br>" );
	    s.append ( "<b>"+"Label:"+"</b> <input type=\"text\" name=\"label\" size=\"30\" maxLength=\"100\" value=\"" );
	    if ( this.getLabel () != null )
	      s.append ( HTMLUtils.encode ( this.getLabel () ) );
	    s.append ( "\"> <font color=\"#999999\">"+"(empty or any text including HTML)"+"</font><br><br>" );

	    if ( inputErrorId != null && inputErrorId.equals ( "invalidSize" ) )
	      s.append ( "<br><font color=\"#ff0000\"><b>"+"Error!"+"</b></font> "+"(empty or any text including HTML)"+"<br><br>" );
	    s.append ( "<b>"+"Visible width in characters:"+"</b>\n" );
	    s.append ( "<input type=\"text\" name=\"size\" class='span1' size=\"3\" maxLength=\"3\" value=\"" + this.getSize () + "\"> " );
	    s.append ( "<font color=\"#999999\">(" + minSize + " "+"to"+" " + maxSize + ")</font>" );
	    s.append ( "<br><br>" );

	    if ( inputErrorId != null && inputErrorId.equals ( "invalidMaxLength" ) )
	      s.append ( "<br><font color=\"#ff0000\"><b>"+"Error!"+"</b></font> "+"Please enter a valid number for the max. number of characters a user can type in."+"<br><br>" );
	    s.append ( "<b>"+"Maximum number of characters:"+"</b>\n" );
	    s.append ( "<input type=\"text\" name=\"maxLength\" size=\"3\" class='span1' maxLength=\"3\"  value=\"" );
	    if ( this.getMaxLength () > 0 )
	      s.append ( String.valueOf ( this.getMaxLength () ) );
	    s.append ( "\"> " );
	    s.append ( "<font color=\"#999999\">(" + minMaxLength + " "+"to"+" " + maxMaxLength + ")</font>" );
	    s.append ( "<br><br>" );
	    s.append ( super.getEditFormEndHTML_New () );

	    return s.toString ();
	  }
  
  public String makeEditFormChanges ( HttpServletRequest request ) {
	    super.makeEditFormChanges ( request );

	    try {
	      this.setLabel ( (String) request.getParameter ( "label" ) );
	    }
	    catch ( Exception e ) { // response to user input error
	      return "invalidLabel";
	    }

	    try {
	      int size = Integer.parseInt ( (String) request.getParameter ( "size" ) );
	      if ( size >= minSize && size <= maxSize )
	        this.setSize ( size );
	      else
	        throw new NumberFormatException ();
	    }
	    catch ( Exception NumberFormatException ) { return "invalidSize"; }

	    try {
	      String maxLengthStr = (String) request.getParameter ( "maxLength" );
	      if ( maxLengthStr == null ) {
	        throw new NumberFormatException ();
	      }
	      else if ( maxLengthStr.equals ( "" ) ) {
	        this.setMaxLength ( 0 );
	      }
	      else {
	        int maxLength = Integer.parseInt ( maxLengthStr );
	        if ( maxLength >= minMaxLength && maxLength <= maxMaxLength )
	          this.setMaxLength ( maxLength );
	        else
	          throw new NumberFormatException ();
	      }
	    }
	    catch ( Exception NumberFormatException ) { return "invalidMaxLength"; }

	    return null; // no user input error occurred
	  }
  

  public String makeEditQuestion ( TbQuestion tbQuestion ) {
	    super.makeEditQuestion ( tbQuestion );

	    try {
	      this.setLabel ( tbQuestion.getQUESTION_LABEL() );
	    }
	    catch ( Exception e ) { // response to user input error
	      return "invalidLabel";
	    }

	    try {
	      int size = Integer.parseInt ( tbQuestion.getTEXTSIZE() );
	      if ( size >= minSize && size <= maxSize )
	        this.setSize ( size );
	      else
	        throw new NumberFormatException ();
	    }
	    catch ( Exception NumberFormatException ) { return "invalidSize"; }

	    try {
	      String maxLengthStr = tbQuestion.getMAXLENGTH();
	      if ( maxLengthStr == null ) {
	        throw new NumberFormatException ();
	      }
	      else if ( maxLengthStr.equals ( "" ) ) {
	        this.setMaxLength ( 0 );
	      }
	      else {
	        int maxLength = Integer.parseInt ( maxLengthStr );
	        if ( maxLength >= minMaxLength && maxLength <= maxMaxLength )
	          this.setMaxLength ( maxLength );
	        else
	          throw new NumberFormatException ();
	      }
	    }
	    catch ( Exception NumberFormatException ) { return "invalidMaxLength"; }

	    return null; // no user input error occurred
	  }

  public String makeEntryChanges ( int sectionNr, int questionNr, HttpServletRequest request ) {
    try {
      this.setValue ( (String) request.getParameter ( "q_" + sectionNr + "_" + questionNr ) );
    }
    catch ( Exception e ) { // response to user input error
      return "error";
    }

    return null; // no input error occurred
  }

  public TbQuestion getTbQuestion ( int mode ) {
	  
	  TbQuestion inputTextline = super.getTbQuestion(mode);
	  	inputTextline.setQUESTION_TYPE("inputTextline");
	  	
	    if ( mode == iSurveyEntryForm.modeEdit || mode == iSurveyEntryForm.modeResultsSummary ) {
	      if (label != null)
	    	  inputTextline.setQUESTION_LABEL(HTMLUtils.xmlFilter(this.getLabel ()) );
	      	  inputTextline.setTEXTSIZE(String.valueOf ( this.getSize ()));
	      	  inputTextline.setMAXLENGTH(String.valueOf ( this.getMaxLength () ));	
	    }
	    if ( mode == iSurveyEntryForm.modeResultsSummary ) {
	    	inputTextline.setRESULT_CNT( this.getCount ());
	    }

	    if ( mode == iSurveyEntryForm.modeEntry ) {
	      inputTextline.setQUESTION_RESULT ( HTMLUtils.xmlFilter(this.getValue ()) );
	    }

	    return inputTextline;
	  }
  
  public void updateResultsSummary ( Question entryQuestion ) {
	if ( !((InputTextline) entryQuestion).getValue ().equals("") ) { // increment count for this question
      this.incrementCount();
      this.setValue( ((InputTextline) entryQuestion).getValue () );
    }
  }

  public void updateResultsSummary ( int sectionId,TbResults entryQuestion ) {
      this.setValue(entryQuestion.getQUESTION_RESULT());
  }
  
  
  public String getResultsDetailsHTML ( Question entryQuestion ) {
	    return ((InputTextline) entryQuestion).getValue ();
	  }


  public String getExport ( Question entryQuestion, String delimiter ) {
    return delimiter;
  }

}
