package edu.vt.ward.survey;

import com.whomade.kycarrots.mgt.survey.vo.TbQuestion;
import com.whomade.kycarrots.mgt.survey.vo.TbResults;
import com.whomade.kycarrots.mgt.survey.vo.TbSurvey;
import jakarta.servlet.http.HttpServletRequest;

public abstract class Question implements Cloneable {
  protected String surveyId;
  private String text = ""; // contains question text etc.
  boolean showDivider = true; // true if the divider between questions should be displayed
  boolean exportInclude = true; // true if the question is to be included in an export data command
  boolean exportExpand = false; // true if the results are to be shown one column per option (instead of default: one column per question)
  private String summaryCd = "0"; // contains question text etc.

  private int questionId = 0; // contains question text etc.
  private String questionType = ""; // contains question text etc.

  
  Question (String surveyId) {
  	this.surveyId = surveyId;
  }

  Question (String surveyId, String text ) {
  	this.surveyId = surveyId;
    this.setText ( text );
  }

  

public int getQuestionId() {
	return questionId;
}

public void setQuestionId(int questionId) {
	this.questionId = questionId;
}




public String getQuestionType() {
	return questionType;
}

public void setQuestionType(String questionType) {
	this.questionType = questionType;
}

Question (String surveyId, TbQuestion tbQuestion ) {
  	this.surveyId = surveyId;
  	this.questionId= tbQuestion.getQUESTION_ID();
  	this.questionType = tbQuestion.getQUESTION_TYPE();
  	
    try {
      this.setShowDivider (tbQuestion.getSHOWDIVIDER());
    }
    catch ( Exception e ) { }
    try {
      this.setExportInclude ( tbQuestion.getEXPORTINCLUDE().equals("1") );
    }
    
    catch ( Exception e ) { }
    try {
      this.setExportExpand ( tbQuestion.getEXPORTEXPAND().equals("1") );
    }

    catch ( Exception e ) { }
    try {
      this.setSummaryCd ( tbQuestion.getSUMMARY_CD() );
    }
    
    catch ( Exception e ) { }
    try {
      this.setText ( edu.vt.ward.survey.HTMLUtils.newLineDecode( tbQuestion.getQUESTIONTEXT() ) );
    }
    catch ( Exception e ) { }
  }

  public String getSurveyId() { return surveyId; }
  
  public String getText () {
    return text;
  }

  public void setText ( String text ) {
    this.text = text;
  }

  public boolean getShowDivider () {
    return showDivider;
  }

  public String getShowDividerAsString () {
    if ( showDivider ) {
      return "1";
    }
    else {
      return "0";
    }
  }

  public void setShowDivider ( boolean showDivider ) {
    this.showDivider = showDivider;
  }

  public void setShowDivider ( String showDivider ) {
    if ( showDivider != null && showDivider.equals ( "1" ) ) {
      this.showDivider = true;
    }
    else {
      this.showDivider = false;
    }
  }

  public boolean getExportInclude () {
    return exportInclude;
  }

  public void setExportInclude ( boolean exportInclude ) {
    this.exportInclude = exportInclude;
  }

  public boolean getExportExpand () {
    return exportExpand;
  }

  public void setExportExpand ( boolean exportExpand ) {
    this.exportExpand = exportExpand;
  }

  public Question copy () {
    try {
      return (Question) super.clone ();				// return the clone
    } catch (CloneNotSupportedException e) {
       throw new InternalError();
    }
  }

  public String getHTML_New( TbSurvey survey, int mode, int sectionNr, int questionNr ) {
	 StringBuffer s = new StringBuffer ();
     s.append("");
     return s.toString ();
  }

  
  public String getHTML ( TbSurvey survey, int mode, int sectionNr, int questionNr ) {
	 StringBuffer s = new StringBuffer ();

    if ( mode == edu.vt.ward.survey.iSurveyEntryForm.modeEdit || mode == edu.vt.ward.survey.iSurveyEntryForm.modeResultsExport ) {
      s.append ( "<a name=\"q_" + sectionNr + "_" + questionNr + "\"></a>" );
      s.append ( getTableOpenHTML ( mode, sectionNr, questionNr ) );
    }

    if ( mode == edu.vt.ward.survey.iSurveyEntryForm.modeResultsSummary ) {
      s.append ( "<table cellspacing=\"0\" cellpadding=\"3\" border=\"0\" width=\"100%\"><tr><td class=\"globalsettings\">" );
    }

    if ( !this.getText ().equals ( "" ) )
      s.append ( "<b>" + this.getText () + "</b><br>\n" );

    
    return s.toString ();
  }

 
  public String getEditCopyDeleteUpDownButtonHTML ( int sectionNr, int questionNr ) {
    return edu.vt.ward.survey.EditFormButtons.getEditCopyDeleteButtonHTML ( "editQuestion.jsp?surveyId=" + this.surveyId + "&section=" + sectionNr + "&question=" + questionNr, "edit",
                                                         "copyQuestion.jsp?surveyId=" + this.surveyId + "&section=" + sectionNr + "&question=" + questionNr, "edit",
                                                         "deleteQuestion.jsp?surveyId=" + this.surveyId + "&section=" + sectionNr + "&question=" + questionNr, "delete")
           + "&nbsp;&nbsp;" + this.getUpDownButtonHTML ( sectionNr, questionNr );
  }

  public String getAddAboveButtonsHTML ( int sectionNr, int questionNr ) {
    return edu.vt.ward.survey.EditFormButtons.getAddAboveButtonsHTML ( "addQuestion.jsp?surveyId=" + this.surveyId + "&section=" + sectionNr + "&question=" + questionNr,
                                                    "editQuestion.jsp?surveyId=" + this.surveyId + "&type=comment&section=" + sectionNr + "&question=" + questionNr );
  }

  public String getUpButtonHTML ( int sectionNr, int questionNr ) {
    return edu.vt.ward.survey.EditFormButtons.getUpButtonHTML ( "upQuestion.jsp?surveyId=" + this.surveyId + "&section=" + sectionNr + "&question=" + questionNr, "move up" );
  }

  public String getDownButtonHTML ( int sectionNr, int questionNr ) {
    return edu.vt.ward.survey.EditFormButtons.getDownButtonHTML ( "downQuestion.jsp?surveyId=" + this.surveyId + "&section=" + sectionNr + "&question=" + questionNr, "move down" );
  }

  public String getUpDownButtonHTML ( int sectionNr, int questionNr ) {
    return this.getUpButtonHTML ( sectionNr, questionNr ) + this.getDownButtonHTML ( sectionNr, questionNr );
  }

  public static String getAddButtonsHTML (String surveyId, int sectionNr, int questionNr ) {
    StringBuffer s = new StringBuffer ();

    s.append ( "<a href=\"addQuestion.jsp?surveyId=" + surveyId + "&section=" + sectionNr + "&question=" + questionNr + "\" style=\"font-size:80%\">"+"add question here"+"</a>&nbsp; " );
    s.append ( "<a href=\"editQuestion.jsp?surveyId=" + surveyId + "&type=comment&amp;section=" + sectionNr + "&question=" + questionNr + "\" style=\"font-size:80%\">"+"add text here"+"</a>&nbsp;" );
    s.append ( "<a href=\"addSection.jsp?surveyId=" + surveyId + "&section=" + (sectionNr+1) + "\" style=\"font-size:80%\">Add Section</a>&nbsp;" );
    s.append ( "<a href=\"deleteSection.jsp?surveyId=" + surveyId + "&section=" + (sectionNr) + "\" style=\"font-size:80%\">Delete Section</a>&nbsp; " );
    s.append ( "<a href=\"editSection.jsp?surveyId=" + surveyId + "&section=" + (sectionNr) + "\" style=\"font-size:80%\">Edit Section</a><br> " );

    return s.toString ();
  }

  // this method is overwritten by all sub classes
  public String getEditFormHTML ( int sectionNr, int questionNr, String type, String inputErrorId ) {
    return getQuestionTextHTML ();
  }


  // this method is overwritten by all sub classes
  public String getEditFormHTML_New ( int sectionNr, int questionNr, String type, String above, String inputErrorId ) {
    return getQuestionTextHTML_New ();
  }

  public String getTableOpenHTML ( int mode, int sectionNr, int questionNr ) {

	 StringBuffer s = new StringBuffer ( "" );

    if ( this.getShowDivider () )
      s.append ( "<br>" );

    s.append ( "<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\"><tr><td bgcolor=\"#cccccc\">" );
    s.append ( "<table cellspacing=\"1\" cellpadding=\"2\" border=\"0\" width=\"100%\"><tr><td bgcolor=\"#6699cc\">");
    s.append ( "<img src=\"images/trans.gif\" width=\"600\" height=\"1\" border=\"0\" alt=\"\"><br>" );
    s.append ( "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr><td style=\"color : #ffffff\">" );

    if ( mode == edu.vt.ward.survey.iSurveyEntryForm.modeEdit )
      s.append ( this.getQuestionTypeTextHTML () );
    if ( mode == edu.vt.ward.survey.iSurveyEntryForm.modeResultsExport ) {
      if ( !this.getClass().getName().equals("edu.vt.ward.survey.InputComment") ) {
          s.append ( "<input type=\"checkbox\" name=\"include_" + sectionNr + "_" + questionNr + "\" value=\"1\"");
          if ( this.getExportInclude() )
            s.append ( " checked" );
            s.append ( "> "+"include this question in the export" );
      }
      else {
        s.append ( "&nbsp;" );
      }

      if ( this.getClass().getName ().equals( "edu.vt.ward.survey.InputRadio" ) ||
              this.getClass().getName ().equals( "edu.vt.ward.survey.InputCheckbox" ) ) {
        s.append ( "&nbsp;&nbsp;&nbsp;<input type=\"checkbox\" name=\"expand_" + sectionNr + "_" + questionNr + "\" value=\"1\"");
        if ( this.getExportExpand() )
          s.append ( " checked" );
        s.append ( "> "+"one column per option (default: one column per question)" );
      }
      else {
        s.append ( "&nbsp;" );
      }

    }

    s.append ( "</td><td nowrap align=\"right\">" );
    if ( mode == edu.vt.ward.survey.iSurveyEntryForm.modeEdit ) {
      s.append ( this.getEditCopyDeleteUpDownButtonHTML ( sectionNr, questionNr ) );
      s.append ( "&nbsp;&nbsp;" );
      s.append ( this.getAddAboveButtonsHTML ( sectionNr, questionNr ) );
    }
    s.append ( "</td></tr></table>" );
    s.append ( "</td></tr><tr><td class=\"globalsettings\">" );

    return s.toString ();
  }

  // this method is overwritten by all extended classes
  // outputs text e.g. "Multiple choice - pick one" or "Short answer - one line"
  public String getQuestionTypeTextHTML () {
    return "Question";
  }

  public String getTableCloseHTML () {
    return "</td></tr></table>\n";
  }

  public String getQuestionTextHTML () {
	    StringBuffer s = new StringBuffer ();

	    s.append ( "<b>"+"Question prompt:"+"</b> <font color=\"#999999\">"+"(empty or any text including HTML)"+"</font><br>\n" );
	    s.append ( "<textarea name=\"prompt\" wrap=\"physical\" cols=\"60\" rows=\"3\">" );
	    s.append ( edu.vt.ward.survey.HTMLUtils.encode ( this.getText () ) );
	    s.append ( "</textarea><br><br>\n" );

	    s.append ( this.getDividerQuestionHTML () );

	    return s.toString ();
	  }

  public String getQuestionTextHTML_New () {
	    StringBuffer s = new StringBuffer ();
	    s.append ( "<b>"+"Question prompt:"+"</b> <font color=\"#999999\">"+"(empty or any text including HTML)"+"</font><br>\n" );
	    s.append ( "<textarea name=\"prompt\" wrap=\"physical\" cols=\"60\" class='span12' rows=\"3\">" );
	    s.append ( edu.vt.ward.survey.HTMLUtils.encode ( this.getText () ) );
	    s.append ( "</textarea><br><br>\n" );
	    s.append ( this.getDividerQuestionHTML_New () );

	    return s.toString ();
	  }

  public String getEditFormBeginHTML ( int sectionNr, int questionNr, String type ) {
    return getEditFormBeginHTML ( sectionNr, questionNr, type, true );
  }

  
  public String getEditFormBeginHTML ( int sectionNr, int questionNr, String type, boolean showPrompt ) {
    StringBuffer s = new StringBuffer ("");
    s.append ( "<form action=\"editQuestion.jsp?section=" + sectionNr + "&question=" + questionNr );
    if ( type != null ) { s.append ( "&type=" + type ); }
    s.append ( "\" method=\"post\">\n" );
    s.append ( "<input type=\"hidden\" name=\"surveyId\" value=\"" + this.getSurveyId () + "\">\n" );

    if ( showPrompt ) { s.append( getQuestionTextHTML () ); }

    return s.toString();
  }

  public String getEditFormBeginHTML_New ( int sectionNr, int questionNr, String type, String above ) {
	    return getEditFormBeginHTML_New ( sectionNr, questionNr, type,above, true );
  }
  
  public String getEditFormBeginHTML_New ( int sectionNr, int questionNr, String type, String above, boolean showPrompt ) {
	    StringBuffer s = new StringBuffer ("");
	    s.append ( "<form action=\"/mgt/survey/editQuestion_ok.do?section=" + sectionNr + "&question=" + questionNr );
	    if ( type != null ) { s.append ( "&type=" + type ); }
	    if ( type != null ) { s.append ( "&above=" + above ); }
	    s.append ( "\" method=\"post\">\n" );
	    s.append ( "<input type=\"hidden\" name=\"surveyId\" value=\"" + this.getSurveyId () + "\">\n" );

	    if ( showPrompt ) { s.append( getQuestionTextHTML_New () ); }

	    return s.toString();
	  }
  
  public String getDividerQuestionHTML () {
	    StringBuffer s = new StringBuffer ();

	    s.append ( "<b>"+"Separate this item visually from the previous one?"+"</b><br>" );
	    s.append ( "<font color=\"#999999\">"+"(choosing no allows you to visually merge questions or text items)"+"</font><br>\n" );
	    s.append ( "<input type=\"radio\" name=\"showDivider\" value=\"1\"" );
	    if ( this.getShowDividerAsString ().equals ( "1" ) )
	      s.append ( " checked" );
	    s.append ( "> "+"Yes"+"<br>\n" );
	    s.append ( "<input type=\"radio\" name=\"showDivider\" value=\"0\"" );
	    if ( this.getShowDividerAsString ().equals ( "0" ) )
	      s.append ( " checked" );
	    s.append ( "> "+"No"+"<br>\n" );
	    s.append ( "<br>\n" );

	    return s.toString ();
	  }

  public String getDividerQuestionHTML_New () {
	    StringBuffer s = new StringBuffer ();

	    s.append ( "<b>"+"Separate this item visually from the previous one?"+"</b><br>" );
	    s.append ( "<font color=\"#999999\">"+"(choosing no allows you to visually merge questions or text items)"+"</font><br>\n" );
	    s.append ( "<div class='control-group'>");
   	    s.append ( "<div class='controls'>");
   	    s.append ( "<label>");
	    s.append ( "<input type=\"radio\" name=\"showDivider\" value=\"1\"" );
	    if ( this.getShowDividerAsString ().equals ( "1" ) )
	      s.append ( " checked" );
	    s.append ( ">\n" );
	    s.append ( "<span class='lbl'>"+"Yes"+"</span>");
	    s.append ( "</label>");
	    s.append ( "<label>");
	    s.append ( "<input type=\"radio\" name=\"showDivider\" value=\"0\"" );
	    if ( this.getShowDividerAsString ().equals ( "0" ) )
	      s.append ( " checked" );
	    s.append ( ">\n" );
	    s.append ( "<span class='lbl'>"+"No"+"</span>");
	    s.append ( "</label>");
	    s.append ( "</div>");
	    s.append ( "</div>");
	    s.append ( "<br>\n" );
	    return s.toString ();
	}

  public String getEditFormEndHTML () {
	    return "<input type=\"submit\" class=\"button\" name=\"save\" value=\""+"OK"+"\">&nbsp;&nbsp;<input type=\"submit\" class=\"button\" name=\"cancel\" value=\""+"Cancel"+"\">\n</form>\n";
  }

  public String getEditFormEndHTML_New () {
	    return "<input type=\"submit\" class=\"btn btn-primary\" name=\"save\" value=\""+"OK"+"\">&nbsp;&nbsp;<input type=\"submit\" class=\"btn\" name=\"cancel\" value=\""+"Cancel"+"\">\n</form>\n";
  }

  public String makeEditFormChanges ( HttpServletRequest request ) {
    try {
      this.setShowDivider ( (String) request.getParameter ( "showDivider" ) );
    }
    catch ( Exception e ) { // response to user input error
    }

    String prompt = (String) request.getParameter ( "prompt" );
    if ( prompt != null ) {
      this.setText ( prompt );
    }

    return null; // no input error occurred
  }

  public String makeEditQuestion (TbQuestion tbQuestion) {
	    try {
	      this.setShowDivider (tbQuestion.getSHOWDIVIDER() );
	    }
	    catch ( Exception e ) { // response to user input error
	    }

	    String prompt = tbQuestion.getQUESTIONTEXT();
	    if ( prompt != null ) {
	      this.setText ( prompt );
	    }

	    return null; // no input error occurred
	  }

  
  // this method is overwritten by all extended classes
  public String makeEntryChanges ( int sectionNr, int questionNr, HttpServletRequest request ) {
    return null;
  }

  // place holder; gets overwritten
  public void updateResultsSummary ( Question entryQuestion ) {
  }

  // place holder; gets overwritten
  public void updateResultsSummary (int sectionId,TbResults entryQuestion  ) {
  }

  // this method is overwritten by all extended classes
  public String getResultsDetailsHTML ( Question entryQuestion ) {
    return "";
  }

  // place holder
  public String getExport ( Question entryQuestion, String delimiter ) {
    return "";
  }

  public TbQuestion getTbQuestion ( int mode ) {
	  TbQuestion inputTextline = new TbQuestion();
	    if ( this.getText () != null ) {
	      if ( mode == edu.vt.ward.survey.iSurveyEntryForm.modeEdit || mode == edu.vt.ward.survey.iSurveyEntryForm.modeResultsSummary ) {
	    	inputTextline.setQUESTIONTEXT(edu.vt.ward.survey.HTMLUtils.newLineEncode( edu.vt.ward.survey.HTMLUtils.xmlFilter(text)));
	    	inputTextline.setSHOWDIVIDER(edu.vt.ward.survey.HTMLUtils.xmlFilter(this.getShowDividerAsString ()));
	        if ( this.getExportInclude() ) {
	        	inputTextline.setEXPORTINCLUDE("1");
	       	}else { 
	        	inputTextline.setEXPORTINCLUDE("0");
	       	}
	        
	        if ( this.getExportExpand() ) { 
	        	inputTextline.setEXPORTEXPAND("1");
	        }else { 
	        	inputTextline.setEXPORTEXPAND("0");
	       	}
	      }
	    }

	    return inputTextline;
	  }

  
  static public Question getNewQuestion (String surveyId, String type ) {
    if ( type.equals ( "comment" ) ) {
      return (Question) new edu.vt.ward.survey.InputComment(surveyId);
    }
    else if ( type.equals ( "radio" ) ) {
      return (Question) new edu.vt.ward.survey.InputRadio(surveyId);
    }
    else if ( type.equals ( "checkbox" ) ) {
      return (Question) new edu.vt.ward.survey.InputCheckbox(surveyId);
    }
    else if ( type.equals ( "textline" ) ) {
      return (Question) new edu.vt.ward.survey.InputTextline(surveyId);
    }
    else if ( type.equals ( "textarea" ) ) {
      return (Question) new edu.vt.ward.survey.InputTextarea(surveyId);
    }
    else if ( type.equals ( "textimage" ) ) {
      return (Question) new edu.vt.ward.survey.InputImage(surveyId);
    }
    else if ( type.equals ( "textlocation" ) ) {
      return (Question) new edu.vt.ward.survey.InputLocation(surveyId);
    }
    else {
      return null;
    }
  }
  
  static public Question getNewQuestion(String surveyId, String type,TbQuestion tbQuestion ) {
	  
		System.out.println("*****************getNewQuestion**********************");

	    if ( type.equals ( "comment" ) ) {
	      return (Question) new edu.vt.ward.survey.InputComment(surveyId,tbQuestion);
	    }
	    else if ( type.equals ( "radio" ) ) {
	      return (Question) new edu.vt.ward.survey.InputRadio(surveyId,tbQuestion);
	    }
	    else if ( type.equals ( "checkbox" ) ) {
	      return (Question) new edu.vt.ward.survey.InputCheckbox(surveyId,tbQuestion);
	    }
	    else if ( type.equals ( "textline" ) ) {
	      return (Question) new edu.vt.ward.survey.InputTextline(surveyId,tbQuestion);
	    }
	    else if ( type.equals ( "textarea" ) ) {
	      return (Question) new edu.vt.ward.survey.InputTextarea(surveyId,tbQuestion);
	    }
	    else if ( type.equals ( "textimage" ) ) {
	      return (Question) new edu.vt.ward.survey.InputImage(surveyId,tbQuestion);
	    }
	    else if ( type.equals ( "textlocation" ) ) {
	      return (Question) new edu.vt.ward.survey.InputLocation(surveyId,tbQuestion);
	    }
	    else {
	      return null;
	    }
	  }

	public String getSummaryCd() {
		return summaryCd;
	}
	
	public void setSummaryCd(String summaryCd) {
		this.summaryCd = summaryCd;
	}
  
}