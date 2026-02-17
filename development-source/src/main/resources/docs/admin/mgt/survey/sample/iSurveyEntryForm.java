package edu.vt.ward.survey;

import java.util.*;

import com.whomade.kycarrots.framework.common.object.DataMap;
import com.whomade.kycarrots.mgt.survey.vo.TbQuestion;
import com.whomade.kycarrots.mgt.survey.vo.TbQuestionLabel;
import com.whomade.kycarrots.mgt.survey.vo.TbResults;
import com.whomade.kycarrots.mgt.survey.vo.TbResultsLabel;
import com.whomade.kycarrots.mgt.survey.vo.TbSection;
import com.whomade.kycarrots.mgt.survey.vo.TbSurvey;
import jakarta.servlet.http.HttpServletRequest;
import net.sf.json.JSONObject;
import com.whomade.kycarrots.framework.common.util.StringUtil;

public class iSurveyEntryForm {
  public static final int modeEntry = 0; // everything works
  public static final int modeEdit = 1; // contains all the buttons
  public static final int modePreview = 2; // everything, submit button doesn't work
  public static final int modeResultsSummary = 3; // only for internal purposes, counts number of feedbacks on a certain option
  public static final int modeResultsExport = 4; // form for coding the questions and selecting the ones that are to be exported
  public static final int modeMod = 5; // EntryMod

 
  protected String id;
  protected String title = "";
  protected String bgColor = "#ffffff";
  protected String textColor = "#000000";
  protected String font = "Verdana, Geneva, Arial, Helvetica, sans-serif";
  protected String fontSize = ""; // "12px";
  protected String header = "";
  protected String footer = "";
  protected String baseHref = "";
  protected String userCSS = "";
  protected boolean showBorder = true; // true, if the default web browser border should be shown
  protected String entryAuthUser = ""; // contains the id of a user who authenticated before submitting a survey
  protected boolean exportIncludeQuestions = true; // true, if the question text should be included in export
  protected String exportDelimiter = ";";
  protected String ip = ""; // IP address of the computer that submitted the survey or the one that last edited the entry form
  protected String browserId = ""; // web browser Id string from the browser that submitted the survey or the one that last edited the entry form
  protected long javascriptEnabled = 0;
  protected long count = 0;
  protected String divider = "<br>";
  protected String introductionText = "";
  protected String exitPageText = "&lt;h1&gt;Thank you for your feedback!&lt;/h1&gt;"; // \n<a href=\"JavaScript:this.parent.window.close()\">Close this window</a>";
  private boolean exitPageTextIsHTML = true; // true, if the text is HTML rather than plain text
  protected String urlScheme = "http"; // can also be "https"

  Vector sections = new Vector ();
		  
  private boolean exitSurvey = false; // true, if the text is HTML rather than plain text

  protected String surveyJson;

  public iSurveyEntryForm ( String id) {
	  this.id=id;
  }

  public String getSurveyJaon() {
	   return surveyJson;
  }

  public void sectionJson(ArrayList<TbSection> pSection) {
	  ArrayList<TbSection> sectionsArr = new  ArrayList<TbSection>();

      int questinsize=0;
	  TbSection aTbSection = null;
	  for (int i=0; i<pSection.size(); i++) {
		  aTbSection = new TbSection();
		  aTbSection = (TbSection) pSection.get(i);
		  
		  questinsize = aTbSection.getQUESTIONS().size();
		  for (int j=0; j<questinsize; j++) {
			  TbQuestion aTbQuestion = (TbQuestion) aTbSection.getQUESTIONS().get(j);
			  aTbQuestion.setQUESTION_TYPETEXT_HTML(getSection(i).getQuestion (j).getQuestionTypeTextHTML());
			  aTbQuestion.setHTML_NEW(getSection(i).getQuestion (j).getHTML_New(null,modeEdit,i, aTbQuestion.getQUESTION_ID()));
			  aTbSection.setQUESTIONS(aTbQuestion,j);
		  }
		 sectionsArr.add(aTbSection);
	  }
	 
	  
	  
	  JSONObject resultJSON = new JSONObject();
	  resultJSON.put("SECTIONS", sectionsArr);
	  surveyJson = resultJSON.toString();
  }

  
  
  public boolean exists () {
    return isExitSurvey();
  }
  

  
  public boolean isExitSurvey() {
	return exitSurvey;
}

public void setExitSurvey(boolean exitSurvey) {
	this.exitSurvey = exitSurvey;
}

public String getId() { return id; }
  public String getTitle() { return title; }
  public void setTitle( String title ) {  this.title = new String (title); }
  public String getBgColor() { return this.bgColor; }
  public void setBgColor( String bgColor ) {  this.bgColor = new String (bgColor); }
  public String getTextColor() { return this.textColor; }
  public void setTextColor( String textColor ) {  this.textColor = new String (textColor); }
  public String getFont() { return this.font; }
  public void setFont( String font ) {  this.font = new String (font); }
  public String getFontSize() { return this.fontSize; }
  public void setFontSize( String fontSize ) {  this.fontSize = new String (fontSize); }
  public String getHeader() { return this.header; }
  public void setHeader( String header ) {  this.header = new String (header); }
  public String getFooter () { return this.footer; }
  public void setFooter( String footer ) {  this.footer = new String (footer); }
  public String getBaseHref () { return this.baseHref; }
  public void setBaseHref ( String baseHref ) { this.baseHref = baseHref; }
  public String getUserCSS () { return this.userCSS; }
  public void setUserCSS ( String userCSS ) { this.userCSS = userCSS; }
  public boolean getShowBorder () { return this.showBorder; }
  public void setShowBorder ( boolean showBorder ) { this.showBorder = showBorder; }
  public String getEntryAuthUser () { return this.entryAuthUser; }
  public void setEntryAuthUser ( String entryAuthUser ) {  this.entryAuthUser = entryAuthUser; }
  public String getExportDelimiter () { return this.exportDelimiter; }
  public void setExportDelimiter( String exportDelimiter ) {  this.exportDelimiter = new String (exportDelimiter); }
  public String getIp () { return this.ip; }
  public void setIp( String ip ) {  this.ip = new String (ip); }
  public String getBrowserId () { return this.browserId; }
  public void setBrowserId( String browserId ) {
    if ( browserId != null ) {
      this.browserId = new String (browserId);
    }
    else { this.browserId = "unknown"; }
  }
  public long getJavascriptEnabled () { return this.javascriptEnabled; }
  public void setJavascriptEnabled ( long javascriptEnabled ) {  this.javascriptEnabled = javascriptEnabled; }
  public void incrementJavascriptEnabled () {  this.javascriptEnabled++; }

  public long getCount () { return this.count; }
  public void setCount ( long count ) { 
	  this.count = count; 
   }
  public void incrementCount () {  this.count++; }

  public boolean getExportIncludeQuestions () { return this.exportIncludeQuestions; }
  public void setExportIncludeQuestions ( boolean exportIncludeQuestions ) { this.exportIncludeQuestions = exportIncludeQuestions; }
  public String getDivider () { return this.divider; }
  public void setDivider( String divider ) {  this.divider = new String (divider); }
  public String getExitPageText () { return this.exitPageText; }
  public void setExitPageText ( String exitPageText ) { this.exitPageText = exitPageText; }
  public boolean getExitPageTextIsHTML () { return this.exitPageTextIsHTML; }
  public void setExitPageTextIsHTML ( boolean exitPageTextIsHTML ) { this.exitPageTextIsHTML = exitPageTextIsHTML; }

  public String getIntroductionText () { return this.introductionText; }
  public void setIntroductionText ( String introductionText ) { this.introductionText = introductionText; }

  public void addSection ( iSection section ) {
    sections.add ( section );
  }

  public void addSection ( int index, iSection section ) {
    sections.add ( index, section );
  }

  public void removeSection ( int index ) {
    sections.remove ( index );
  }

  public iSection getSection ( int sectionNr ) {
    return (iSection) this.sections.get ( sectionNr );
  }

  public int getNumSections () {
    return sections.size ();
  }

  public boolean save () {
     return true;
  }

  public boolean saveResultsSummary () {
     return true;
  }

  // this code addresses the problem of two entries arriving at virtually the same time
  private String getUnusedSurveyEntryId () {
    String entryId = "";
    return entryId;
  }

  public String saveEntry () {
     return "";
  }

  public String modEntry (String pEntryId) {
	    return "";
	  }

  public void load (List<DataMap> SectionList, List<DataMap> QuestionList, List<DataMap> QuestionLableList) {
	ArrayList<TbSection> sectionList = new ArrayList<TbSection>();
	ArrayList<TbQuestionLabel> tbQuestionLabels = null;
 
  	for(DataMap aDatasection :SectionList){
		TbSection section = new TbSection();
		section.setSURVEY_ID(this.id);
		section.setSECTION_ID(aDatasection.getInt("SECTION_ID"));
		section.setSECTION_TITLE(aDatasection.getString("SECTION_TITLE"));
		for(DataMap aDataQuestion : QuestionList){
			if(section.getSECTION_ID()==aDataQuestion.getInt("SECTION_ID")){
				TbQuestion question = new TbQuestion(); 
				question.setSURVEY_ID(this.id);
				question.setSECTION_ID(aDataQuestion.getInt("SECTION_ID"));
				question.setQUESTION_ID(aDataQuestion.getInt("QUESTION_ID"));
				question.setQUESTION_TYPE(aDataQuestion.getString("QUESTION_TYPE"));                 
			    question.setSHOWDIVIDER(aDataQuestion.getString("SHOWDIVIDER")); 					
			    question.setEXPORTINCLUDE(aDataQuestion.getString("EXPORTINCLUDE"));                 
			    question.setEXPORTEXPAND(aDataQuestion.getString("EXPORTEXPAND"));					
			    question.setQUESTIONTEXT(aDataQuestion.getString("QUESTIONTEXT"));                	
			    question.setQUESTION_CD(aDataQuestion.getString("QUESTION_CD"));                   
			    question.setQUESTION_ISHTML(aDataQuestion.getString("QUESTION_ISHTML"));				
			    question.setQUESTION_LAYOUT(aDataQuestion.getString("QUESTION_LAYOUT"));               
			    question.setQUESTION_LABEL(aDataQuestion.getString("QUESTION_LABEL"));                
			    question.setOTHERANSWER(aDataQuestion.getString("OTHERANSWER"));					
			    question.setTEXTCOLS(aDataQuestion.getString("TEXTCOLS"));               	 	    
			    question.setTEXTROWS(aDataQuestion.getString("TEXTROWS"));               	 	    
			    question.setTEXTSIZE(aDataQuestion.getString("TEXTSIZE"));               	 	    
			    question.setMAXLENGTH( aDataQuestion.getString("MAXLENGTH"));     
			    question.setRESULT_CNT( aDataQuestion.getLong("RESULT_CNT"));
			    question.setOTHERANSWER_RESULT_CNT(aDataQuestion.getLong("OTHERANSWER_RESULT_CNT"));
			    question.setEXPORTINCLUDE(aDataQuestion.getString("EXPORTINCLUDE"));        
			    question.setSUMMARY_CD(aDataQuestion.getString("SUMMARY_CD"));        
			    
			    
			    if(aDataQuestion.getString("QUESTION_TYPE").equals("inputRadio") || aDataQuestion.getString("QUESTION_TYPE").equals("inputCheckbox")){
				    tbQuestionLabels = new ArrayList<TbQuestionLabel>();
				    for(DataMap aDataQuestioLabel :QuestionLableList){
				    	
				    	//System.out.println("SECTION_ID["+aDataQuestioLabel.getInt("SECTION_ID")+"]QUESTION_ID["+aDataQuestioLabel.getInt("QUESTION_ID")+"]"+"]QUESTION_LABEL_ID["+aDataQuestioLabel.getInt("QUESTION_LABEL_ID")+"]");
				    	
				    	if(question.getSECTION_ID()==aDataQuestioLabel.getInt("SECTION_ID") && question.getQUESTION_ID() == aDataQuestioLabel.getInt("QUESTION_ID")){
					    	//System.out.println("TRUE SECTION_ID["+aDataQuestioLabel.getInt("SECTION_ID")+"]QUESTION_ID["+aDataQuestioLabel.getInt("QUESTION_ID")+"]"+"]QUESTION_LABEL_ID["+aDataQuestioLabel.getInt("QUESTION_LABEL_ID")+"]");
				    		TbQuestionLabel questionLabel = new TbQuestionLabel();
				    		questionLabel.setSURVEY_ID(this.id);
				    		questionLabel.setSECTION_ID(aDataQuestioLabel.getInt("SECTION_ID"));
				    		questionLabel.setQUESTION_ID(aDataQuestioLabel.getInt("QUESTION_ID"));
				    		questionLabel.setQUESTION_LABEL_ID(aDataQuestioLabel.getInt("QUESTION_LABEL_ID"));
				    		questionLabel.setQUESTION_LABEL(aDataQuestioLabel.getString("QUESTION_LABEL"));
				    		questionLabel.setEXPORTCODE(aDataQuestioLabel.getString("EXPORTCODE"));
				    		questionLabel.setSELECTED(aDataQuestioLabel.getString("SELECTED"));
				    		questionLabel.setRESULT_CNT( aDataQuestioLabel.getLong("RESULT_CNT"));
				    		
				    		tbQuestionLabels.add(questionLabel);
				    	}
				    }
				    question.setQUESTIONLABELS(tbQuestionLabels);
			    }

			    section.addQUESTIONS(question);
			}
		}

		iSection aSection = new iSection(this,section); 
		addSection(aSection);
	    sectionList.add(section); //json 추가
			
  	}
  	sectionJson(sectionList); //json 추가
  
  }

  public String getExitPageHTML ( int mode ) {
    StringBuffer s = new StringBuffer ();
    String style = "font-family : " + this.getFont ();
    if ( !this.getFontSize ().equals ( "" ) ) {
      style += "; font-size : " + this.getFontSize ();
    }
    String styleColors = "background-color : " + this.getBgColor() + "; color : " + this.getTextColor ();

    if ( mode != modeEdit ) {
      s.append ( "<html>\n" );
      s.append ( "<head>\n" );
      s.append ( "  <title>" + this.getTitle () + "</title>\n" );
      s.append ( "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=" + "EUC-KR" + "\">\n" );
      s.append ( "  <style>\n" );
      s.append ( "    body, p, td { " + style + " }\n" );
//      s.append ( "    h1 { font-size : 28px; font-weight : normal }\n" );
      s.append ( "  </style>\n" );
      if ( !this.getBaseHref().equals("") ) {
        s.append ( "  <base href=\"" + this.getBaseHref() + "\">\n" );
      }
      if ( !this.getUserCSS().equals("") ) {
        s.append ( "  <link type=\"text/css\" rel=\"stylesheet\" media=\"screen\" href=\"" + this.getUserCSS() + "\">\n" );
      }
      s.append ( "</head>\n" );
      s.append ( "<body" );

      if ( !this.getBgColor ().equals ( "" ) )
        s.append ( " bgColor=\"" + bgColor + "\"" );
      if ( !this.getTextColor ().equals ( "" ) )
        s.append ( " text=\"" + this.getTextColor () + "\"" );

      s.append ( " onLoad=\"this.window.focus()\"" );
      if ( !this.getShowBorder() )
        s.append ( " leftMargin=\"0\" topMargin=\"0\" marginheight=\"0\" marginwidth=\"0\"" );

      s.append ( ">\n" );

      if ( this.getFont().startsWith("Verdana") ) { s.append ( "<font size=\"2\">\n" ); }
      s.append ( this.getHeader () + "<br>\n" );
      if ( this.getFont().startsWith("Verdana") ) { s.append ( "</font><font size=\"2\">\n" ); }
    } // end: if ( mode != modeEdit )

    if ( mode == modeEdit ) {
      s.append ( "  <style>\n" );
      s.append ( "    .globalsettings { " + style + "; " + styleColors + " }\n" );
      s.append ( "  </style>\n");
      s.append ( "<table cellspacing=\"0\" cellpadding=\"4\" border=\"0\" width=\"100%\">");
      s.append ( "<tr><td colspan=\"2\"><img src=\"images/trans.gif\" width=\"600\" height=\"1\" border=\"0\" alt=\"\"><br></td></tr>" );
      s.append ( "<tr><td bgcolor=\"#ff9900\"><b>" );
      s.append ("Exit Page" );
      s.append ( "</b></td><td bgcolor=\"#ff9900\" nowrap align=\"right\">" + this.getEditGlobalSettingsButtonHTML ( "editExitPage.jsp?surveyId=" + this.getId() ) + "</td></tr></table>" );
      s.append ( "<br>\n" );
      s.append ( "The following text is displayed after the user submits the survey entry form:"+"<br><br>\n" );
      s.append ( "<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\"><tr><td bgcolor=\"#cccccc\">" );
      s.append ( "<table cellspacing=\"1\" cellpadding=\"2\" border=\"0\" width=\"100%\"><tr><td bgcolor=\"#6699cc\">");
      s.append ( "<img src=\"images/trans.gif\" width=\"600\" height=\"1\" border=\"0\" alt=\"\"><br>" );
      s.append ( "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr><td style=\"color : #ffffff\">" );
      s.append ( "Text" );
      s.append ( "</td><td nowrap align=\"right\">" + this.getExitPageSettingsButtonHTML () + "</td></tr></table>" );
      s.append ( "</td></tr><tr><td class=\"globalsettings\">" );
    }

    if ( this.getExitPageTextIsHTML () ) {
      s.append ( this.getExitPageText () );
    }
    else {
      s.append ( HTMLUtils.newLine2Br ( HTMLUtils.encode ( this.getExitPageText () ) ) );
    }

    if ( mode == modeEdit ) {
      s.append ( "</td></tr></table></td></tr></table>\n" );
    }

    if ( mode != modeEdit ) {
      s.append ( "<br><br><br><a href=\""+"http"+"://" + "localhost:8080"+ "/survey/"+ "\"><img src=\""+"http"+"://" + "localhost:8080" + "/survey/"+ "images/createdwith.gif\" border=\"0\" alt=\""+"created with"+" " + "Survey" + "\"></a><br>" );
      if ( this.getFont().startsWith("Verdana") ) { s.append ( "</font><font size=\"2\">\n" ); }
      s.append ( this.getFooter () );
      if ( this.getFont().startsWith("Verdana") ) { s.append ( "</font>\n" ); }
      s.append ( "</body>\n" );
      s.append ( "</html>\n" );
    }

    return s.toString ();
  }

  public String getExitPageHTML_New ( int mode ) {
	    StringBuffer s = new StringBuffer ();
	    
	    if ( this.getExitPageTextIsHTML () ) {
	      s.append ( this.getExitPageText () );
	    }
	    else {
	      s.append ( HTMLUtils.newLine2Br ( HTMLUtils.encode ( this.getExitPageText () ) ) );
	    }

	    return s.toString ();
	  }

  public String getHTML ( TbSurvey survey, int mode ) {
    StringBuffer s = new StringBuffer ();
    for (int i=0;i<this.getNumSections();i++){
        s.append ( this.getSection ( i ).getHTML ( survey, mode, i ) );
    }
    return s.toString ();
  }

  public String getExport ( Question entryQuestion, String delimiter ) {
    StringBuffer s = new StringBuffer ();
    s.append ( this.getSection ( 0 ).getExport ( entryQuestion, delimiter ) );
    return s.toString ();
  }



  private String getExitPageSettingsButtonHTML () {
    return " <a href=\"editExitPageSettings.jsp?surveyId=" + this.getId() + "\" style=\"font-size:80%\">"+"edit exit page text"+"</a>\n";
  }

  private String getEditGlobalSettingsButtonHTML ( String returnTo ) {
    return " <a href=\"editGlobalSettings.jsp?surveyId=" + this.getId() + "&returnTo=" + java.net.URLEncoder.encode(returnTo) + "\" style=\"font-size:80%\">"+"edit header, footer, font, colors"+"</a>\n";
  }


  // goes through all entry files and updates the "count" attribute for each option, text field etc.
  public void createResultsSummary () {
    // loop through all entry files
   }

  public void delete () {
  }

  public String makeEntryChanges ( HttpServletRequest request ) {
    try {
      
	     for ( int i = 0; i < sections.size(); i++ ) {
	        String errorId = ((iSection) sections.get(i)).makeEntryChanges ( i, request );
	        if ( errorId != null )
	          throw new Exception ();
	      }
	      this.setIp ( request.getRemoteAddr() );
	      this.setBrowserId( (String) request.getHeader ( "User-Agent") );
	      if ( request.getParameter("javascriptEnabled") != null ) { this.setJavascriptEnabled( 1 ); }
	      else { this.setJavascriptEnabled( 0 ); }
    }
    catch ( Exception e ) { // response to user input error
      return "error";
    }

    return null; // no input error occurred
  }

  // reads an entry file; returns true if it could read the file
  public boolean readEntryResults ( String entryId,List<DataMap> QuestionResult,List<DataMap> QuestionResultLabel ) {
		 ArrayList<TbResults>tbResults = new  ArrayList<TbResults>();
		 ArrayList<TbResultsLabel> tbTbResultsLabels = null;
		
		for (DataMap aDataResult : QuestionResult){
		  TbResults aResult = new TbResults(); 
		  aResult.setRESULTS_ID(aDataResult.getInt("RESULTS_ID"));
		  aResult.setSURVEY_ID(aDataResult.getString("SURVEY_ID"));
		  aResult.setSECTION_ID(aDataResult.getInt("SECTION_ID"));
		  aResult.setQUESTION_ID(aDataResult.getInt("QUESTION_ID"));
		  aResult.setQUESTION_RESULT(aDataResult.getString("QUESTION_RESULT"));  				
		  aResult.setOTHERANSWER_RESULT(aDataResult.getString("OTHERANSWER_RESULT"));           	
		  aResult.setINSERT_DATE(aDataResult.getString("INSERT_DATE"));      
		  tbTbResultsLabels = new ArrayList<TbResultsLabel>();
		    for(DataMap aDataResultLabel :QuestionResultLabel){
	    		TbResultsLabel resultLabel = new TbResultsLabel();
		    	if(aResult.getRESULTS_ID()==aDataResultLabel.getInt("RESULTS_ID")&&aResult.getSECTION_ID()==aDataResultLabel.getInt("SECTION_ID") && aResult.getQUESTION_ID() == aDataResultLabel.getInt("QUESTION_ID")){
		    		resultLabel.setRESULTS_ID(aDataResult.getInt("RESULTS_ID"));
		    		resultLabel.setRESULTS_LABEL_ID(aDataResult.getInt("RESULTS_LABEL_ID"));
		    		resultLabel.setSURVEY_ID(aDataResult.getString("SURVEY_ID"));
		    		resultLabel.setSECTION_ID(aDataResultLabel.getInt("SECTION_ID"));
		    		resultLabel.setQUESTION_ID(aDataResultLabel.getInt("QUESTION_ID"));
		    		resultLabel.setQUESTION_LABEL_ID(aDataResultLabel.getInt("QUESTION_LABEL_ID"));
		    		resultLabel.setQUESTION_LABEL_RESULT(aDataResultLabel.getString("QUESTION_LABEL_RESULT")); 
					//System.out.println("**************resultLabel.getRESULTS_LABEL_ID()+["+resultLabel.getQUESTION_LABEL_ID()+"/"+aDataResultLabel.getInt("QUESTION_LABEL_ID")+"/"+resultLabel.getQUESTION_LABEL_RESULT()+"]********************************");

		    		tbTbResultsLabels.add(resultLabel);
		    	}
		    }
		  aResult.setTbResultsLabels(tbTbResultsLabels);
		  tbResults.add(aResult);
	  }
		
	  for (int irow=0;irow<this.getNumSections();irow++){
			this.getSection ( irow ).updateResultsSummary ( irow, tbResults );
	  }
      return true;
  }

  public boolean isReadyForOpen () {
    boolean ready = false;
    for ( int i=0; ready == false && i < this.getSection( 0 ).getNumQuestions(); i++ ) {
      Question q = this.getSection( 0 ).getQuestion(i);
      if ( !q.getClass().getName().equals("edu.vt.ward.survey.InputComment") )
        ready = true;
    }
    return ready;
  }

  public String getResultsStr (TbResults pTbResult ) {
	 if(pTbResult==null) return "";
	 String sReturn=""; 
     StringBuffer s = new StringBuffer ("");
	 
	if (pTbResult.getQUESTION_TYPE().equals ( "inputComment" ) ) {
		 sReturn = "";
    }else if (pTbResult.getQUESTION_TYPE().equals ( "inputTextline" ) ) {
		 sReturn = pTbResult.getQUESTION_RESULT();
    }else if ( pTbResult.getQUESTION_TYPE().equals ( "inputTextarea" ) ) {
		 sReturn = pTbResult.getQUESTION_RESULT();
    }else if ( pTbResult.getQUESTION_TYPE().equals ( "inputImage" ) ) {
		 sReturn = pTbResult.getQUESTION_RESULT();
    }else if ( pTbResult.getQUESTION_TYPE().equals ( "inputLocation" ) ) {
		 sReturn = pTbResult.getQUESTION_RESULT();
    }else if ( pTbResult.getQUESTION_TYPE().equals ( "inputRadio" ) || pTbResult.getQUESTION_TYPE().equals ( "inputCheckbox" )) {
    	 s = new StringBuffer (""); 
    	 
 
    	for (TbResultsLabel aTbResultsLabel: pTbResult.getTbResultsLabels()){
	         if ( aTbResultsLabel.getQUESTION_LABEL_RESULT().equals("1") ) {
	             s.append (aTbResultsLabel.getQUESTION_LABEL()+ "\n");
	           }
			 
		 }
    	 
	    if ( !(pTbResult.getOTHERANSWER_RESULT().equals("")) ) {
	      s.append ( pTbResult.getOTHERANSWER() + " " + pTbResult.getOTHERANSWER_RESULT() + "\n" );
	    }

		 sReturn = s.toString();
    }
	
	 return sReturn;
  }
  
  public String getExportStr (TbResults pTbResult ,String delimiter) {
	 if(pTbResult==null) return "";
	 String sReturn=""; 
     StringBuffer s = new StringBuffer ("");
	 
	if (pTbResult.getQUESTION_TYPE().equals ( "inputComment" ) ) {
		 sReturn = "";
    }else if (pTbResult.getQUESTION_TYPE().equals ( "inputTextline" ) ) {
 		 sReturn =StringUtil.translateExportDelimiters(pTbResult.getQUESTION_RESULT(),delimiter);
    }else if ( pTbResult.getQUESTION_TYPE().equals ( "inputTextarea" ) ) {
		 sReturn =StringUtil.translateExportDelimiters(pTbResult.getQUESTION_RESULT(),delimiter);
    }else if ( pTbResult.getQUESTION_TYPE().equals ( "inputImage" ) ) {
		 sReturn =StringUtil.translateExportDelimiters(pTbResult.getQUESTION_RESULT(),delimiter);
    }else if ( pTbResult.getQUESTION_TYPE().equals ( "inputLocation" ) ) {
		 sReturn =StringUtil.translateExportDelimiters(pTbResult.getQUESTION_RESULT(),delimiter);
    }else if ( pTbResult.getQUESTION_TYPE().equals ( "inputRadio" ) || pTbResult.getQUESTION_TYPE().equals ( "inputCheckbox" )) {
    	 s = new StringBuffer (""); 
    	 
	   boolean firstSelected = true;

	   int i=0;
    	for (TbResultsLabel aTbResultsLabel: pTbResult.getTbResultsLabels()){
	         if ( aTbResultsLabel.getQUESTION_LABEL_RESULT().equals("1") ) {
		    		if ( !firstSelected ) {
		    			if ( delimiter.equals(",")) { s.append("; "); } else { s.append(", "); }
		    		}
		        firstSelected = false;
		        s.append ( StringUtil.translateExportDelimiters (""+(i+1), delimiter));
	         }
		     i++;
		 }
    	 
	   
    	if ( !(pTbResult.getOTHERANSWER_RESULT().equals("")) ) {
	        /* 
    		if ( !firstSelected ) {
		          if ( delimiter.equals(",")) { s.append("; "); } else { s.append(", "); }
		     }
		     */
		     s.append ( StringUtil.translateExportDelimiters ( pTbResult.getOTHERANSWER()+ " ", delimiter ) );
		     s.append ( StringUtil.translateExportDelimiters ( pTbResult.getOTHERANSWER_RESULT() , delimiter ) );
	    }
      
		 sReturn = s.toString();
    }
	
	 return sReturn;
  }
  
  
  
}
