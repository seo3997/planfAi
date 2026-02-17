package edu.vt.ward.survey;


import com.whomade.kycarrots.mgt.survey.vo.TbQuestion;
import com.whomade.kycarrots.mgt.survey.vo.TbResults;
import com.whomade.kycarrots.mgt.survey.vo.TbSurvey;
import jakarta.servlet.http.HttpServletRequest;

public class InputLocation extends Question {
  private static int minSize = 1;
  private static int maxSize = 100;
  private static int minMaxLength = 1;
  private static int maxMaxLength = 300;
  private int size = 30;
  private int maxLength = 300;
  private String label = "";
  private String value = ""; // contains the text the user inputs
  private long count = 0;

  public InputLocation (String surveyId) {
    super (surveyId);
  }

  public InputLocation (String surveyId, String text, String label ) {
    super (surveyId, text );
    this.setLabel ( label );
  }

  public InputLocation (String surveyId, TbQuestion tbQuestion ) {
    super (surveyId, tbQuestion ); // assign questionText


    try { this.setValue (tbQuestion.getQUESTIONTEXT()  ); }
    catch ( Exception e ) { }

    try { this.setLabel ( tbQuestion.getQUESTION_LABEL() ); }
    catch ( Exception e ) { }

    try { this.setSize ( Integer.parseInt ( tbQuestion.getTEXTSIZE() ) ); }
    catch ( Exception e ) { }

    try { this.setMaxLength ( Integer.parseInt (  tbQuestion.getMAXLENGTH() ) ); }
    catch ( Exception e ) { }
    try { this.setCount (tbQuestion.getRESULT_CNT() ); }
    catch ( Exception e ) { }
  }

  // outputs text e.g. "Multiple choice - pick one" or "Short answer - one line"
  public String getQuestionTypeTextHTML () {
    return "Input Location";
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
      String sQuestinId="q_" + sectionNr + "_" + questionNr;
	  StringBuffer s = new StringBuffer ();
		if ( mode == iSurveyEntryForm.modeEntry || mode == iSurveyEntryForm.modePreview ){

			s.append ("<dd>\n");
			s.append ("<div class='control-group'>\n");
			s.append ("<label class='control-label' for='form-field-1'>"+this.getText()+"</label>\n");
			s.append ("<div class='input-append'>\n");
		    s.append ( "<input type=\"text\" name=\"q_" + sectionNr + "_" + questionNr + "\"" );
		    if ( this.getSize () > 0 )
		      s.append ( " size=\"" + String.valueOf ( this.getSize () ) + "\"" );
		    if ( this.getMaxLength () > 0 )
		      s.append ( " maxLength=\"" + String.valueOf ( this.getMaxLength () ) + "\"" );
		      
		    s.append ( " value=\"" + this.getValue() + "\"" );
		    
		    s.append ( ">\n");
			
			String[] sa=new String[4];
			sa[0]="";
			sa[1]="";
			sa[2]="";
			sa[3]="";

		    
		    s.append ("<input type='hidden' name='"+sQuestinId+"_Lat'  value='" + sa[0] + "' >\n");
    		s.append ("<input type='hidden' name='"+sQuestinId+"_Lan'  value='" + sa[1] + "' >\n");
    		s.append ("<button type='button' class='btn btn-small btn-primary' onClick='fn_popLocation_"+sQuestinId+"()'>Map</button>\n");
    		s.append ("<script> \n");
    		s.append ("function fn_popLocation_"+sQuestinId+"() {\n");
    		s.append ("	var goUrl=\"/isurvey/location.jsp?latitude=\""+"+form."+sQuestinId+"_Lat.value+"+"\"&longitude=\"+form."+sQuestinId+"_Lan.value+"+"\"&ptext="+sQuestinId+"\";\n");
    		s.append ("	SmallWin = window.open(goUrl, 'popLocation','scrollbars=yes,resizable=yes,toolbar=no,height=600,width=800');\n");
    	    s.append ("	SmallWin.opener.name = 'SubLocation'; \n");
    	    s.append ("}\n");		
    	    s.append ("</script>\n");
    	    s.append ("</div>\n");
    	    s.append ("</div>\n");
    	    s.append ("</dd>\n");
			
		}
		
		if( mode == iSurveyEntryForm.modeEdit || mode == iSurveyEntryForm.modeResultsExport){
		    s.append ("<div class=\"control-group\">");
		    s.append ("<label class=\"control-label\" for=\"form-field-1\">"+this.getText()+"</label>");
		    s.append ("<div class='controls'>"+this.getLabel ());
		    s.append ( "<input type=\"text\" name=\"q_" + sectionNr + "_" + questionNr + "\" value=\"\"" );
		    if ( this.getSize () > 0 )
		      s.append ( " size=\"" + String.valueOf ( this.getSize () ) + "\"" );
		    if ( this.getMaxLength () > 0 )
		      s.append ( " maxLength=\"" + String.valueOf ( this.getMaxLength () ) + "\"" );
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
	          //s.append ( "<span class=\"highlightresponses\">" + this.getValue () + "</span>" );
	         
				String sLocation = this.getValue ();
				String[] sa=new String[4];
				sa[0]="";
				sa[1]="";
				sa[2]="";
				sa[3]="";
				
				if ( sLocation.length() != 0 ) {
					sa = sLocation.split(" ");
					s.append("<a href=\"javascript:fn_popLocation('"+sa[0]+"','"+sa[1]+"')\">"+sa[0]+","+sa[1]+"</a>");
				}else{
			          s.append ( "<span class=\"highlightresponses\">" + this.getValue () + "</span>" );
				}
			    
	        	
	        }
	        else {
	          s.append ( "<span class=\"highlightresponses\"><i>"+"no answer"+"</i></span>" );
	        }
	      }
	      else {
	        s.append ( "<span class=\"highlightresponses\"><b>" );
	        s.append ( String.valueOf ( this.getCount () ) + " ");
	        if ( this.getCount() == 1 ) { s.append ("response" ); } else { s.append ( "responses" ); }
	        s.append ( "</b></span>" );
	        if ( this.getCount() > 0 ) {
	          s.append (" &nbsp;&nbsp;<a href=\"/mgt/survey/viewResultsQuestion.do?surveyId=" + this.surveyId + "&section=" + sectionNr + "&question=" + questionNr + "\">"+"이 질문보기"+"</a>&nbsp; <a href=\"/mgt/survey/viewResultsDetails.do?surveyId=" + this.surveyId + "&section=" + sectionNr + "&question=" + questionNr + "\">"+"모든 질문보기"+"</a>");
	        }
	      } // end: else: if ( survey.singleEntry )
	    } // end: if ( mode == SurveyEntryForm.modeResultsSummary )

		if ( mode == iSurveyEntryForm.modeMod){
			s.append ("<dd>\n");
			s.append ("<div class='control-group'>\n");
			s.append ("<label class='control-label' for='form-field-1'>"+this.getText()+"</label>\n");
			s.append ("<div class='input-append'>\n");
		    s.append ( "<input type=\"text\" name=\"q_" + sectionNr + "_" + questionNr + "\"" );
		    if ( this.getSize () > 0 )
		      s.append ( " size=\"" + String.valueOf ( this.getSize () ) + "\"" );
		    if ( this.getMaxLength () > 0 )
		      s.append ( " maxLength=\"" + String.valueOf ( this.getMaxLength () ) + "\"" );
		      
		    s.append ( " value=\"" + this.getValue() + "\"" );
		    
		    s.append ( ">\n");
			
			String sLocation = this.getValue ();
			String[] sa=new String[4];
			sa[0]="";
			sa[1]="";
			sa[2]="";
			sa[3]="";
			if ( sLocation.length() != 0 ) {
				sa = sLocation.split(" ");
			}

		    
		    s.append ("<input type='hidden' name='"+sQuestinId+"_Lat'  value='" + sa[0] + "' >\n");
    		s.append ("<input type='hidden' name='"+sQuestinId+"_Lan'  value='" + sa[1] + "' >\n");
    		s.append ("<button type='button' class='btn btn-small btn-primary' onClick='fn_popLocation_"+sQuestinId+"()'>Map</button>\n");
    		s.append ("<script> \n");
    		s.append ("function fn_popLocation_"+sQuestinId+"() {\n");
    		s.append ("	var goUrl=\"/isurvey/location.jsp?latitude=\""+"+form."+sQuestinId+"_Lat.value+"+"\"&longitude=\"+form."+sQuestinId+"_Lan.value+"+"\"&ptext="+sQuestinId+"\";\n");
    		s.append ("	SmallWin = window.open(goUrl, 'popLocation','scrollbars=yes,resizable=yes,toolbar=no,height=600,width=800');\n");
    	    s.append ("	SmallWin.opener.name = 'SubLocation'; \n");
    	    s.append ("}\n");		
    	    s.append ("</script>\n");
    	    s.append ("</div>\n");
    	    s.append ("</div>\n");
    	    s.append ("</dd>\n");
			
			
			
		}

		return s.toString();
  }
  
 
  public String getEditFormHTML_New ( int sectionNr, int questionNr, String type,String above, String inputErrorId ) {
	    StringBuffer s = new StringBuffer ();

	    s.append ( "<h2>" + this.getQuestionTypeTextHTML () + "</h2>\n" );
	    s.append ( "Hint: You can leave Question prompt empty to make a short answer field; seem to belong to the previous question, e.g. first name, last name." );

	    s.append ( super.getEditFormBeginHTML_New ( sectionNr, questionNr, type,above ) );

	    if ( inputErrorId != null && inputErrorId.equals ( "invalidLabel" ) )
	      s.append ( "<br><font color=\"#ff0000\"><b>"+"Error!"+"</b></font> "+"Please enter a valid label."+"<br><br>" );
	    s.append ( "<b>"+"Label:"+"</b> <input type=\"text\" name=\"label\" size=\"30\" maxLength=\"100\" value=\"" );
	    if ( this.getLabel () != null )
	      s.append ( HTMLUtils.encode ( this.getLabel () ) );
	    s.append ( "\"> <font color=\"#999999\">"+"(empty or any text including HTML)"+"</font><br><br>" );

	    if ( inputErrorId != null && inputErrorId.equals ( "invalidSize" ) )
	      s.append ( "<br><font color=\"#ff0000\"><b>"+"Error!"+"</b></font> "+"Please enter a valid number for the visible width of the input field."+"<br><br>" );
	    s.append ( "<b>"+"Visible width in characters:"+"</b>\n" );
	    s.append ( "<input type=\"text\" name=\"size\" size=\"3\" class='span1' maxLength=\"3\" value=\"" + this.getSize () + "\"> " );
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
	  	inputTextline.setQUESTION_TYPE("inputLocation");
	  	
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
    if ( !((InputLocation) entryQuestion).getValue ().equals("") ) { // increment count for this question
      this.incrementCount();
      this.setValue( ((InputLocation) entryQuestion).getValue () );
    }
  }
  public void updateResultsSummary (int sectionId, TbResults entryQuestion ) {
      this.setValue(entryQuestion.getQUESTION_RESULT());
  }

  public String getResultsDetailsHTML ( Question entryQuestion ) {
	    return ((InputLocation) entryQuestion).getValue ();
	  }


  public String getExport ( Question entryQuestion, String delimiter ) {
    return delimiter;
  }

}
