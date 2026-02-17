package edu.vt.ward.survey;


import com.whomade.kycarrots.mgt.survey.vo.TbQuestion;
import com.whomade.kycarrots.mgt.survey.vo.TbResults;
import com.whomade.kycarrots.mgt.survey.vo.TbSurvey;
import jakarta.servlet.http.HttpServletRequest;

public class InputTextarea extends Question {
  private static int minCols = 10;
  private static int maxCols = 100;
  private static int minRows = 2;
  private static int maxRows = 50;

  private int cols = 60;
  private int rows = 3;
  private String value = ""; // contains the text the user inputs
  private long count = 0;

  public InputTextarea (String surveyId) {
    super (surveyId);
  }

  public InputTextarea (String surveyId, String text, int cols, int rows ) {
    super (surveyId, text );
    this.setCols ( cols );
    this.setRows ( rows );
  }

  public InputTextarea (String surveyId,  TbQuestion tbQuestion) {
    super (surveyId, tbQuestion ); // assign questionText

    try { this.setValue ( HTMLUtils.newLineDecode( tbQuestion.getQUESTIONTEXT()) ); }
    catch ( Exception e ) { this.setValue("Test"); }

    try { this.setCols ( Integer.parseInt ( tbQuestion.getTEXTSIZE() ) ); }
    catch ( Exception e ) { }

    try { this.setRows ( Integer.parseInt (tbQuestion.getTEXTROWS() ) ); }
    catch ( Exception e ) { }

    try { this.setCount (tbQuestion.getRESULT_CNT()); }
    catch ( Exception e ) { }
  }

  // outputs text e.g. "Multiple choice - pick one" or "Short answer - one line"
  public String getQuestionTypeTextHTML () {
    return "Comment/Essay question"; //"Short Answer - multiple lines";
  }

  public String getValue () {
    return this.value;
  }

  public void setValue ( String value ) {
    this.value = value;
  }

  public int getCols () {
    return this.cols;
  }

  public void setCols ( int cols ) {
    this.cols = cols;
  }

  public int getRows () {
    return this.rows;
  }

  public void setRows ( int rows ) {
    this.rows = rows;
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
	    	s.append("<dd>");
		    s.append("<div class='control-group'>");
		    s.append("<label class='control-label' for='form-field-1'>"+this.getText()+"</label>");
		    s.append("<div class='controls'>");
			s.append( "<textarea class='span12' name=\"q_" + sectionNr + "_" + questionNr + "\" wrap=\"virtual\" cols=\"" + String.valueOf( this.getCols () ) + "\" rows=\"" + String.valueOf( this.getRows () ) + "\" ></textarea>\n" );
			s.append("</div>");
			s.append("</div>");
			s.append("</dd>");
    	}
    	if ( mode == iSurveyEntryForm.modeEdit || mode == iSurveyEntryForm.modeResultsExport){
		    s.append("<div class='control-group'>");
		    s.append("<label class='control-label' for='form-field-1'>"+this.getText()+"</label>");
		    s.append("<div class='controls'>");
			s.append( "<textarea class='span12' name=\"q_" + sectionNr + "_" + questionNr + "\" wrap=\"virtual\" cols=\"" + String.valueOf( this.getCols () ) + "\" rows=\"" + String.valueOf( this.getRows () ) + "\" ></textarea>\n" );
			s.append("</div>");
			s.append("</div>");
		   
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

              if ( this.getCount() == 1 ) { s.append ( "response"); } else { s.append ( "responses" ); }
              s.append ( "</b></span>" );
              if ( this.getCount() > 0 ) {
                s.append (" &nbsp;&nbsp;<a href=\"/mgt/survey/viewResultsQuestion.do?surveyId=" + this.getSurveyId() + "&section=" + sectionNr + "&question=" + questionNr + "\">"+"이 질문보기"+"</a>&nbsp; <a href=\"/mgt/survey/viewResultsDetails.do?surveyId=" + this.getSurveyId() + "&section=" + sectionNr + "&question=" + questionNr + "\">"+"모든 질문보기"+"</a>");
              }
            } // end: else: if ( survey.singleEntry )
          }

    	if ( mode == iSurveyEntryForm.modeMod ){
	    	s.append("<dd>");
		    s.append("<div class='control-group'>");
		    s.append("<label class='control-label' for='form-field-1'>"+this.getText()+"</label>");
		    s.append("<div class='controls'>");
			s.append( "<textarea class='span12' name=\"q_" + sectionNr + "_" + questionNr + "\" wrap=\"virtual\" cols=\"" + String.valueOf( this.getCols () ) + "\" rows='" + String.valueOf( this.getRows () ) + "'>"+this.getValue()+"</textarea>\n" );
			s.append("</div>");
			s.append("</div>");
			s.append("</dd>");
    	}
    	return s.toString();
  }
	  



  public String getEditFormHTML_New ( int sectionNr, int questionNr, String type,String above, String inputErrorId ) {
	    StringBuffer s = new StringBuffer ();

	    s.append ( "<h2>" + this.getQuestionTypeTextHTML () + "</h2>\n" );
	    s.append ( super.getEditFormBeginHTML_New ( sectionNr, questionNr, type ,above) );

	    if ( inputErrorId != null && inputErrorId.equals ( "invalidCols" ) )
	      s.append ( "<br><font color=\"#ff0000\"><b>"+"Error!"+"</b></font> "+"Please enter a valid number for the width of the input field."+"<br><br>" );
	    s.append ( "<b>"+"Width in characters:"+"</b>\n" );
	    s.append ( "<input type=\"text\" name=\"cols\" size=\"3\" class='span1' maxlength=\"3\" value=\"" + this.getCols () + "\"> " );
	    s.append ( "<font color=\"#999999\">(" + minCols + " "+"to"+" " + maxCols + ")</font>" );
	    s.append ( "<br><br>" );

	    if ( inputErrorId != null && inputErrorId.equals ( "invalidRows" ) )
	      s.append ( "<br><font color=\"#ff0000\"><b>"+"Error!"+"</b></font> "+"Please enter a valid number for the height of the input field."+"<br><br>" );
	    s.append ( "<b>"+"Number of lines:"+"</b>\n" );
	    s.append ( "<input type=\"text\" name=\"rows\" size=\"2\" class='span1' maxlength=\"2\"  value=\"" + this.getRows () + "\"> " );
	    s.append ( "<font color=\"#999999\">("+ minRows +" "+"to"+" " + maxRows + ")</font>" );
	    s.append ( "<br><br>" );

	    s.append ( "</textarea><br><br>\n" );
	    s.append ( super.getEditFormEndHTML_New() );

	    return s.toString ();
	  }
  
  public String makeEditFormChanges ( HttpServletRequest request ) {
    super.makeEditFormChanges ( request );

    try {
      int cols = Integer.parseInt ( (String) request.getParameter ( "cols" ) );
      if ( cols >= minCols && cols <= maxCols )
        this.setCols ( cols );
      else
        throw new NumberFormatException ();
    }
    catch ( Exception NumberFormatException ) { return "invalidCols"; }

    try {
      int rows = Integer.parseInt ( (String) request.getParameter ( "rows" ) );
      if ( rows >= minRows && rows <= maxRows )
        this.setRows ( rows );
      else
        throw new NumberFormatException ();
    }
    catch ( Exception NumberFormatException ) { return "invalidRows"; }

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
	  
	  TbQuestion inputTextarea = super.getTbQuestion(mode);
	  inputTextarea.setQUESTION_TYPE("inputTextarea");
	  	

	    if ( mode == iSurveyEntryForm.modeEdit || mode == iSurveyEntryForm.modeResultsSummary ) {
	    	inputTextarea.setTEXTCOLS(this.getCols ()+"");
	    	inputTextarea.setTEXTROWS(this.getRows ()+"");
	      }

	    if ( mode == iSurveyEntryForm.modeResultsSummary ) {
	    	inputTextarea.setRESULT_CNT( this.getCount ());
	      }

	      if ( mode == iSurveyEntryForm.modeEntry || mode == iSurveyEntryForm.modeResultsSummary ) {
	        inputTextarea.setQUESTION_RESULT( HTMLUtils.newLineEncode( HTMLUtils.xmlFilter(this.getValue ()) ) );
	      }

	    return inputTextarea;
	  }
  
  
  public void updateResultsSummary ( Question entryQuestion ) {
    if ( !((InputTextarea) entryQuestion).getValue ().equals("") ) { // increment count for this question
      this.incrementCount();
      this.setValue( ((InputTextarea) entryQuestion).getValue () );
    }
  }
  public void updateResultsSummary (int sectionId,TbResults entryQuestion ) {
      this.setValue(entryQuestion.getQUESTION_RESULT());
  }

  public String getResultsDetailsHTML ( Question entryQuestion ) {
	    return ((InputTextarea) entryQuestion).getValue ();
  }


  public String getExport ( Question entryQuestion, String delimiter ) {
    return delimiter;
  }

}
