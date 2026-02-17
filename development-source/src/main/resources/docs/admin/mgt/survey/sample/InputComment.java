package edu.vt.ward.survey;


import com.whomade.kycarrots.mgt.survey.vo.TbQuestion;
import com.whomade.kycarrots.mgt.survey.vo.TbSurvey;
import jakarta.servlet.http.HttpServletRequest;

public class InputComment extends Question {
  private String comment = ""; // contains the text that gets displayed as the comment
  private boolean isHTML = false; // true, if the comment is HTML rather than plain text

  public InputComment (String surveyId) {
    super (surveyId);
  }

  public InputComment (String surveyId, String comment ) {
    super (surveyId, comment );
  }

  public InputComment (String surveyId, TbQuestion tbQuestion ) {
    super (surveyId, tbQuestion ); // assign questionText

  
    try { this.setComment ( HTMLUtils.newLineDecode (  tbQuestion.getQUESTION_LABEL() ) ); }
    catch ( Exception e ) { }

    try { this.setIsHTML (tbQuestion.getQUESTION_ISHTML().equals ( "1" ) ); }
    catch ( Exception e ) { }
  }

  // outputs text e.g. "Multiple choice - pick one" or "Short answer - one line"
  public String getQuestionTypeTextHTML () {
    return "Text";
  }

  public String getComment () {
    return this.comment;
  }

  public void setComment ( String comment ) {
    this.comment = comment;
  }

  public boolean getIsHTML () {
    return this.isHTML;
  }

  public void setIsHTML ( boolean isHTML ) {
    this.isHTML = isHTML;
  }
  public String getHTML_New (  TbSurvey survey, int mode, int sectionNr, int questionNr ) {
	StringBuffer s = new StringBuffer ();
    s.append ( super.getHTML_New ( survey, mode, sectionNr, questionNr ) ); // get HTML for text of the question
    
    if ( mode == iSurveyEntryForm.modeEntry || mode == iSurveyEntryForm.modePreview ){
    	s.append ("<dt><i class=\"icon-caret-right blue\"></i>"+this.comment+"</dt>");
    }
	if( mode == iSurveyEntryForm.modeEdit || mode == iSurveyEntryForm.modeResultsExport){
    	s.append (this.comment);
    }

	if( mode == iSurveyEntryForm.modeResultsSummary){
    	s.append (this.comment);
    }
	
	if( mode == iSurveyEntryForm.modeMod){
    	s.append (this.comment);
    }
    	
    return s.toString();
  }
 
 
 
  public String getEditFormHTML_New ( int sectionNr, int questionNr, String type,String above, String inputErrorId ) {
	    StringBuffer s = new StringBuffer ();

//	    s.append ( "<h2>" + this.getQuestionTypeTextHTML () + "</h2>\n" );
	    s.append ( super.getEditFormBeginHTML_New ( sectionNr, questionNr, type, above,false ) );

	    s.append ( "<b>"+"Text"+":</b> \n" );
	    s.append ( "<input type=\"radio\" name=\"texttype\" value=\"plain\"" );
	    s.append ( " checked" ); 
	    //if ( !this.getIsHTML () ) { s.append ( " checked" ); }
	    s.append ( "> "+"Plain Text"+" <br>" );
	    //2013.07.14 soohyun.Seo
	    //s.append ( "> "+Config.msg(258)+" &nbsp;&nbsp;&nbsp;" );
	    //s.append ( "<input type=\"radio\" name=\"texttype\" value=\"html\"" );
	    //if ( this.getIsHTML () ) { s.append ( " checked" ); }
	    //s.append ( "> "+Config.msg(259)+"<br>" );
	    
	    s.append ( "<textarea name=\"comment\" wrap=\"virtual\" cols=\"60\" class='span12' rows=\"15\">" + HTMLUtils.encode ( this.getComment () ) + "</textarea>\n" );
	    s.append ( "<br><br>" );
	    s.append ( super.getDividerQuestionHTML_New () );
	    s.append ( super.getEditFormEndHTML_New () );

	    return s.toString ();
	  }
  
  public String makeEditFormChanges ( HttpServletRequest request ) {
	super.makeEditFormChanges ( request );
    try {
    		String sComment=(String) request.getParameter ("comment");
    		sComment = sComment.replaceAll("(\r\n|\n)", "<br/>");
    		this.setComment ( sComment);
    }
    catch ( Exception e ) { // response to user input error
      return "invalidComment";
    }

    try {
      if ( ((String) request.getParameter ( "texttype" )).equals ( "html" ) ) {
        this.setIsHTML ( true );
      }
      else {
        this.setIsHTML ( false );
      }
    }
    catch ( Exception e ) { // response to user input error
      return "invalidTextType";
    }

    return null; // no user input error occurred
  }

  public String makeEntryChanges ( int sectionNr, int questionNr, HttpServletRequest request ) {
    return null; // no input error occurred
  }

  public TbQuestion getTbQuestion ( int mode ) {
	  
	  TbQuestion inputComment = super.getTbQuestion(mode);
	  inputComment.setQUESTION_TYPE("inputComment");
	  	
	    if ( mode == iSurveyEntryForm.modeEdit ) {
	        if ( this.getIsHTML () ) {
	          inputComment.setQUESTION_ISHTML( "1" );
	        }
	        else {
	        	inputComment.setQUESTION_ISHTML( "0" );
	        }
	        inputComment.setQUESTION_LABEL( HTMLUtils.newLineEncode ( HTMLUtils.xmlFilter(this.getComment ()) ) );
	      }

	    return inputComment;
	  }

  
}
