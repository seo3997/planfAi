package edu.vt.ward.survey;

import java.util.*;


import com.whomade.kycarrots.mgt.survey.vo.TbQuestion;
import com.whomade.kycarrots.mgt.survey.vo.TbQuestionLabel;
import com.whomade.kycarrots.mgt.survey.vo.TbResults;
import com.whomade.kycarrots.mgt.survey.vo.TbResultsLabel;
import com.whomade.kycarrots.mgt.survey.vo.TbSurvey;
import jakarta.servlet.http.HttpServletRequest;

public abstract class OptionList extends Question {
  private static int sizeOtherShortAnswerLabel = 30;
  private static int maxLengthOtherShortAnswerLabel = 1000;
  private int minOptions = 1;
  private String inputType = "radio"; // other valid value is "checkbox"

  private String layout = "vertical"; // other valid values are "horizontalTextRight" and "horizontalTextAbove" and "horizontalTextBelow"
  private String otherShortAnswerLabel = null; // contains null or name of label for "other:" input field
  private String otherShortAnswer = ""; // contains the value of the "other" field that the user entered
  private long otherShortAnswerCount = 0;
  
 
  
  protected Vector options = new Vector ();

  OptionList (String surveyId) {
    super (surveyId);
  }

  OptionList (String surveyId, String text ) {
    super (surveyId, text );
  }

  OptionList (String surveyId, TbQuestion tbQuestion, String questionType ) {
    super (surveyId, tbQuestion ); // assign questionText

 
    try { this.setLayout ( tbQuestion.getQUESTION_LAYOUT() ); }
    catch ( Exception e ) { }

    try {
      this.setOtherShortAnswer ( tbQuestion.getOTHERANSWER_RESULT() );
      
      if( tbQuestion.getOTHERANSWER()==null ||  tbQuestion.getOTHERANSWER().equals("")){
    	     this.setOtherShortAnswerLabel ( null);
      }else{
    	  this.setOtherShortAnswerLabel (  tbQuestion.getOTHERANSWER());
      }
      
      this.setOtherShortAnswerCount (tbQuestion.getOTHERANSWER_RESULT_CNT() );
    }
    catch ( Exception e ) { }
    
    List<TbQuestionLabel> optionElems = tbQuestion.getQUESTIONLABELS();
    if ( optionElems != null ) {
      for(TbQuestionLabel optionElem :optionElems){
          Option o = new Option ();

          try { o.setQuestionLableId ( optionElem.getQUESTION_LABEL_ID() ); }
          catch ( Exception e ) { }

          try { o.setLabel ( optionElem.getQUESTION_LABEL() ); }
          catch ( Exception e ) { }

          try { o.setExportCode ( optionElem.getEXPORTCODE() ); }
          catch ( Exception e ) { }

          try { o.setSelected ( optionElem.getSELECTED().equals ( "1" ) ); }
          catch ( Exception e ) { }

          try { o.setCount ( optionElem.getRESULT_CNT() ); }
          catch ( Exception e ) { }
          
          this.addOption ( o );
      }
    }
    
  }

  public Vector getOptions () {
    return this.options;
  }

  public int getNumOptions () {
    return this.options.size();
  }

  protected int getMinOptions () {
    return this.minOptions;
  }

  protected void setMinOptions ( int minOptions ) {
    this.minOptions = minOptions;
  }

  protected String getInputType () {
    return this.inputType;
  }

  protected void setInputType ( String inputType ) {
    this.inputType = inputType;
  }

  public String getLayout () {
    return this.layout;
  }

  public void setLayout ( String layout ) {
    this.layout = layout;
  }

  public String getOtherShortAnswer () {
    return this.otherShortAnswer;
  }

  public void setOtherShortAnswer ( String otherShortAnswer ) {
    this.otherShortAnswer = otherShortAnswer;
  }

  public String getOtherShortAnswerLabel () {
    return this.otherShortAnswerLabel;
  }

  public void setOtherShortAnswerLabel ( String otherShortAnswerLabel ) {
    this.otherShortAnswerLabel = otherShortAnswerLabel;
  }

  public long getOtherShortAnswerCount () {
    return this.otherShortAnswerCount;
  }

  public void setOtherShortAnswerCount ( long otherShortAnswerCount ) {
    this.otherShortAnswerCount = otherShortAnswerCount;
  }


  public void addOption ( Option option ) {
    options.add( option );
  }

  public void addOption ( int index, Option option ) {
    options.add( index, option );
  }

  public void removeOption ( int index ) {
    options.remove ( index );
  }

  private String getOtherShortAnswerLabel_NewHTML ( int mode, int sectionNr, int questionNr ) {
	    if ( this.getOtherShortAnswerLabel () != null )  {
	      StringBuffer s = new StringBuffer ();
	      String fieldName = "q_" + String.valueOf(sectionNr) + "_" + String.valueOf(questionNr);

	      if ( mode == iSurveyEntryForm.modeMod  ) {
		      s.append ("<label>");
		      if ( this.getInputType ().equals ( "radio" ) ) {
		        s.append ( "<input type=\"radio\" name=\"" + fieldName + "\" id=\"" + fieldName + "_" + options.size () + "\" value=\"" + options.size () + "\"");
		        if (this.getOtherShortAnswerCount() > 0 ) {
		          s.append ( " checked" );
		        }
		          s.append ( " onClick=\"check_" + fieldName + "(this); document.form." + fieldName + "_other.focus();\"" );
		        s.append ( ">" );
		      }
			  s.append ("<span class='lbl'>"+this.getOtherShortAnswerLabel()+"</span>");
	
		      if ( !this.getOtherShortAnswerLabel ().equals ( "" ) )  s.append ( " " );
		      
		      s.append ( "<input type=\"text\" name=\"" + fieldName + "_other\" size=\"" + sizeOtherShortAnswerLabel + "\" maxLength='" + maxLengthOtherShortAnswerLabel +"'"); 
		      s.append ( " value='"+this.getOtherShortAnswer()+"'");
		      s.append ( " >" );
		      s.append ("</label>");
	      }else{
		      s.append ("<label>");
		      if ( this.getInputType ().equals ( "radio" ) ) {
		        s.append ( "<input type=\"radio\" name=\"" + fieldName + "\" id=\"" + fieldName + "_" + options.size () + "\" value=\"" + options.size () + "\"");
		        if ( mode == iSurveyEntryForm.modePreview || mode == iSurveyEntryForm.modeEntry ) {
		          s.append ( " checked onClick=\"check_" + fieldName + "(this); document.form." + fieldName + "_other.focus();\"" );
		        }
		        s.append ( ">" );
		      }
			     s.append ("<span class='lbl'>"+this.getOtherShortAnswerLabel()+"</span>");
		      if ( !this.getOtherShortAnswerLabel ().equals ( "" ) )  s.append ( " " );
		      s.append ( "<input type=\"text\" name=\"" + fieldName + "_other\" size=\"" + sizeOtherShortAnswerLabel + "\" maxLength=\"" + maxLengthOtherShortAnswerLabel + "\">" );
		      s.append ("</label>");
	    	  
	      }
	      
	      if ( this.getInputType ().equals ( "radio" ) && ( mode == iSurveyEntryForm.modePreview || mode == iSurveyEntryForm.modeEntry ) ) {
	        // output Javascript that disables the "other" field if the radio button doesn't have the focus
	        s.append ( "<script language=\"JavaScript\" type=\"text/javascript\"><!--\n" );
	        s.append ( "function check_" + fieldName + " ( o ) {\n" );
	        s.append ( "  if ( o.value == \"" + options.size() + "\" ) { enable_" + fieldName + "(); }\n" );
	        s.append ( "  else { disable_" + fieldName + "(); }\n" );
	        s.append ( "}\n" );
	        s.append ( "function enable_" + fieldName + "() {\n" );
	        s.append ( "  document.form." + fieldName + "_other.disabled = false;\n" );
	        s.append ( "  document.form." + fieldName + "_other.style.backgroundColor = \"#ffffff\";\n" );
	        s.append ( "}\n" );
	        s.append ( "function disable_" + fieldName + "() {\n" );
	        s.append ( "  document.form." + fieldName + "_other.disabled = true;\n" );
	        s.append ( "  document.form." + fieldName + "_other.style.backgroundColor = \"#eeeeee\";\n" );
	        s.append ( "}\n" );
	        s.append ( "//-->\n" );
	        s.append ( "</script>\n" );
	      }

	      return s.toString ();
	    }
	    else
	      return "";
	  }

  public String getHTML_New( TbSurvey survey, int mode, int sectionNr, int questionNr ) {
	    StringBuffer s = new StringBuffer ();
	    if ( this.getLayout () == null || this.getLayout ().equals ( "vertical" ) )
	    {
	    	if ( mode == iSurveyEntryForm.modeEntry || mode == iSurveyEntryForm.modePreview ){
			    s.append ("<dd>\n");
			    s.append ("	<div class='span12'>\n");
			    s.append ("		<div class='control-group'>\n");
			    s.append ("			<label class='control-label'>"+this.getText()+"</label>\n");
			    for ( int i = 0; i < options.size (); i++ ) {
			    	s.append ("			<label>\n");
			    	s.append ("				<input type=\"" + this.getInputType () + "\" name=\"q_" + sectionNr + "_" + questionNr + "\" value=\"" + i + "\">" );
			    	s.append ("				<span class='lbl'>"+((Option) options.get(i)).getLabel ()+"</span>                                        ");
			    	s.append ("			</label>\n");
			    }
			    if (this.getOtherShortAnswerLabel ()!=null && !this.getOtherShortAnswerLabel ().equals("") ) {
			        s.append ( getOtherShortAnswerLabel_NewHTML ( mode, sectionNr, questionNr ) );
			    }
			    s.append ("		</div>\n");
			    s.append ("	</div>\n");
			    s.append ("</dd>\n");
	    	}
	    	
	    	if ( mode == iSurveyEntryForm.modeMod ){
			    s.append ("<dd>\n");
			    s.append ("	<div class='span12'>\n");
			    s.append ("		<div class='control-group'>\n");
			    s.append ("			<label class='control-label'>"+this.getText()+"</label>\n");
			    for ( int i = 0; i < options.size (); i++ ) {
			    	s.append ("			<label>\n");
			    	s.append ("				<input type=\"" + this.getInputType () + "\" name=\"q_" + sectionNr + "_" + questionNr + "\" value='" + i + "'");
			    	if( ((Option) options.get(i)).getCount () > 0)
			    	s.append ("	checked" );
			    	s.append ("	>" );
			    	s.append ("				<span class='lbl'>"+((Option) options.get(i)).getLabel ()+"</span>                                        ");
			    	s.append ("			</label>\n");
			    }
			    if (this.getOtherShortAnswerLabel ()!=null && !this.getOtherShortAnswerLabel ().equals("") ) {
			        s.append ( getOtherShortAnswerLabel_NewHTML ( mode, sectionNr, questionNr ) );
			    }
			    s.append ("		</div>\n");
			    s.append ("	</div>\n");
			    s.append ("</dd>\n");
	    	}

	    	
	    	if ( mode == iSurveyEntryForm.modeEdit || mode == iSurveyEntryForm.modeResultsExport ){
			    s.append ("	<div class='span12'>\n");
			    s.append ("		<div class='control-group'>\n");
			    s.append ("			<label class='control-label'>"+this.getText()+"</label>\n");
			    for ( int i = 0; i < options.size (); i++ ) {
			    	s.append ("			<label class='block-label'>\n");
			    	s.append ("				<input type=\"" + this.getInputType () + "\" name=\"q_" + sectionNr + "_" + questionNr + "\" value=\"" + i + "\">" );
			    	s.append ("				<span class='lbl'>"+((Option) options.get(i)).getLabel ()+"</span>                                        ");
			    	s.append ("			</label>\n");
			    }
			    if (this.getOtherShortAnswerLabel ()!=null && !this.getOtherShortAnswerLabel ().equals("") ) {
			        s.append ( getOtherShortAnswerLabel_NewHTML ( mode, sectionNr, questionNr ) );
			    }
			    s.append ("		</div>\n");
			    s.append ("	</div>\n");
			   
			    s.append ("<script language=\"JavaScript\" type=\"text/javascript\"><!--\n");
			    for ( int i = 0; i < options.size (); i++ ) {
			        s.append ("document.form.q_" + sectionNr + "_" + questionNr + "[" + i + "].disabled = true;\n" );
			    }
			    if ( this.getOtherShortAnswerLabel () != null )  {
			          if ( this.getClass().getName().equals("edu.vt.ward.survey.InputRadio") ) {
			            s.append ("document.form.q_" + sectionNr + "_" + questionNr + "[" + options.size () + "].disabled = true;\n" );
			          }
			          s.append ("document.form.q_" + sectionNr + "_" + questionNr + "_other.disabled = true;\n" );
			    }
			    s.append ("//-->\n</script>\n");

	    	}
	    
	    }else{
	    	if ( mode == iSurveyEntryForm.modeEntry || mode == iSurveyEntryForm.modePreview ){
			    s.append ("<dd>\n");																									
			    s.append ("	<div class='span12'>\n");
			    s.append ("		<div class='control-group'>\n");
			    s.append ("			<label class='control-label'>"+this.getText()+"</label> ");
			    s.append ("			<div class='span10'>\n");
			    for ( int i = 0; i < options.size (); i++ ) {
			    s.append ("			<div class='span2'>\n");
			    s.append ("			<label>\n");
		    	s.append ("				<input type=\"" + this.getInputType () + "\" name=\"q_" + sectionNr + "_" + questionNr + "\" value=\"" + i + "\">" );
			    s.append ("				<span class='lbl'>"+((Option) options.get(i)).getLabel ()+"</span>                                                                        ");
			    s.append ("			</label>\n");
			    s.append ("			</div>\n");
			    }
			    s.append ("		</div>\n");
			    if (this.getOtherShortAnswerLabel ()!=null && !this.getOtherShortAnswerLabel ().equals("") ) {
				    s.append ("	<div class='span10'>\n");
			    	s.append ( getOtherShortAnswerLabel_NewHTML ( mode, sectionNr, questionNr ) );
				    s.append ("	</div>\n");
			    }
			    s.append ("	</div>\n");
			    s.append ("	</div>\n");
	    	}
	    	
	    	if ( mode == iSurveyEntryForm.modeMod ){
			    s.append ("<dd>\n");																									
			    s.append ("	<div class='span12'>\n");
			    s.append ("		<div class='control-group'>\n");
			    s.append ("			<label class='control-label'>"+this.getText()+"</label> ");
			    s.append ("			<div class='span10'>\n");
			    for ( int i = 0; i < options.size (); i++ ) {
			    s.append ("			<div class='span2'>\n");
			    s.append ("			<label>\n");
		    	s.append ("				<input type=\"" + this.getInputType () + "\" name=\"q_" + sectionNr + "_" + questionNr + "\" value='" + i + "'");

		    	if( ((Option) options.get(i)).getCount () > 0)
		    	s.append ("	checked" );
		    	s.append (" >" );

		    	s.append ("				<span class='lbl'>"+((Option) options.get(i)).getLabel ()+"</span>                                                                        ");
			    s.append ("			</label>\n");
			    s.append ("			</div>\n");
			    }
			    s.append ("		</div>\n");
			    if (this.getOtherShortAnswerLabel ()!=null && !this.getOtherShortAnswerLabel ().equals("") ) {
				    s.append ("	<div class='span10'>\n");
			    	s.append ( getOtherShortAnswerLabel_NewHTML ( mode, sectionNr, questionNr ) );
				    s.append ("	</div>\n");
			    }
			    s.append ("	</div>\n");
			    s.append ("	</div>\n");
	    	}

	    	if ( mode ==  iSurveyEntryForm.modeEdit || mode == iSurveyEntryForm.modeResultsExport ){
			    s.append ("	<div class='span12'>\n");
			    s.append ("		<div class='control-group'>\n");
			    s.append ("			<label class='control-label'>"+this.getText()+"</label> ");
			    s.append ("			<div class='span10'>\n");
			    for ( int i = 0; i < options.size (); i++ ) {
			    s.append ("			<div class='span2'>\n");
			    s.append ("			<label>\n");
		    	s.append ("				<input type=\"" + this.getInputType () + "\" name=\"q_" + sectionNr + "_" + questionNr + "\" value=\"" + i + "\">" );
			    s.append ("				<span class='lbl'>"+((Option) options.get(i)).getLabel ()+"</span>                                                                        ");
			    s.append ("			</label>\n");
			    s.append ("			</div>\n");
			    }
			    s.append ("		</div>\n");
			    if (this.getOtherShortAnswerLabel ()!=null && !this.getOtherShortAnswerLabel ().equals("") ) {
				    s.append ("	<div class='span10'>\n");
			    	s.append ( getOtherShortAnswerLabel_NewHTML ( mode, sectionNr, questionNr ) );
				    s.append ("	</div>\n");
			    }
			    s.append ("	</div>\n");
			    s.append ("	</div>\n");
	    	
			    s.append ("<script language=\"JavaScript\" type=\"text/javascript\"><!--\n");
			    for ( int i = 0; i < options.size (); i++ ) {
			        s.append ("document.form.q_" + sectionNr + "_" + questionNr + "[" + i + "].disabled = true;\n" );
			    }
			    if ( this.getOtherShortAnswerLabel () != null )  {
			          if ( this.getClass().getName().equals("edu.vt.ward.survey.InputRadio") ) {
			            s.append ("document.form.q_" + sectionNr + "_" + questionNr + "[" + options.size () + "].disabled = true;\n" );
			          }
			          s.append ("document.form.q_" + sectionNr + "_" + questionNr + "_other.disabled = true;\n" );
			    }
			    s.append ("//-->\n</script>\n");
	    	
	    	}
	    	
	    }
	    
	    if ( mode == iSurveyEntryForm.modeResultsSummary ) {
	          s.append ( "<table cellpadding=\"2\" cellspacing=\"2\" border=\"0\">\n" );
	          // output the option text
	          for ( int i = 0; i < options.size (); i++ ) {
		          s.append ( "<tr>\n<td>\n" );
		          s.append ( "<label for=\"q_" + sectionNr + "_" + questionNr + "_" + i + "\">" );
		          s.append ( ((Option) options.get(i)).getLabel () );
		          s.append ( "</label>" );
		          s.append ( "\n" );
		          s.append ( "</td>\n" );
		          
		          s.append ( "<td align=\"right\" valign=\"middle\">\n" ); //  
		          if ( survey.singleEntry ) {
			          if ( ((Option) options.get(i)).getCount () > 0 ) {
			              s.append ( "<img alt=\"\" src=\"/common/img/checkmark.gif\" style='height: 17px;width:15px' width=\"15\" height=\"17\">" );
			          }else {
			              s.append ( "<img alt=\"\" src=\"/common/img/trans.gif\" style='height: 17px;width:15px' width=\"15\" height=\"17\">" );
			          }
		          }
		          else {
		            if ( ((Option) options.get(i)).getCount () > 0  )
		            	s.append ( "<a href=\"/mgt/survey/viewResultsDetails.do?surveyId=" + this.surveyId + "&section=" + sectionNr + "&question=" + questionNr + "&option=" + i + "\">" );
		            	s.append ( "<b>" + ((Option) options.get(i)).getCount () + "</b>" );
		            if ( ((Option) options.get(i)).getCount () > 0 )  s.append ( "</a>" );
		          }
		          s.append ( "</td>\n" );
		          
		          // output percentages and bar chart
		          if ( !survey.singleEntry ) {
		            s.append ( "<td align=\"right\">\n" ); 
		            int percent = 0;
		            if ( survey.getNUMENTRIES() > 0 )   percent = Math.round ( (float) ((Option) options.get(i)).getCount () / (float) survey.getNUMENTRIES() * 100 );
		            s.append ( " (" );
		            if ( percent < 10 ) { s.append("&nbsp;"); }
		            s.append ( Integer.toString(percent) + "%)" );
		            s.append ( "</td>\n");
		            // output bar chart
		            s.append ( "<td align=\"left\">\n" );
		            if ( percent > 0 ) {
		              s.append ( "<img src=\"/common/img/resultsbar.gif\" style='height: 10px;width: "+percent+"px;' height=\"10\" width=\""+percent+"\">" );
		            }
		            s.append ( "</td>\n");
		          }
		          s.append ( "</tr>\n" );
	          } // end: for
	    
	          if ( mode == iSurveyEntryForm.modeResultsSummary && this.getOtherShortAnswerLabel () != null ) {
	              s.append ( "<tr>\n" );
	              s.append ( "<td>\n" );

	              if ( !this.getOtherShortAnswerLabel ().equals("") ) {
	                s.append ( this.getOtherShortAnswerLabel () );
	              }
	              else {
	                s.append( "other...");
	              }

	              if ( survey.singleEntry ) {
	                if ( this.getOtherShortAnswerCount () > 0 ) {
	                  s.append( " " + this.getOtherShortAnswer() + "\n" );
	                }
	              }
	              s.append ( "</td>\n" );

	              s.append ( "<td align=\"right\">\n" ); // class=\"highlightresponses\"
	              if ( survey.singleEntry ) {
	                if ( this.getOtherShortAnswerCount() > 0 ) {
	                  s.append ( "<img alt=\"\" src=\"/common/img/checkmark.gif\" style='height: 17px;width:15px' width=\"15\" height=\"17\">" );
	                }
	                else {
	                  s.append ( "<img alt=\"\" src=\"/common/img/trans.gif\" style='height: 17px;width:15px' width=\"15\" height=\"17\">" );
	                }
	              }
	              else {
	                if ( this.getOtherShortAnswerCount () > 0  )
	                  s.append ( "<a href=\"/mgt/survey/viewResultsDetails.do?surveyId=" + this.surveyId + "&section=" + sectionNr + "&question=" + questionNr + "&option=" + options.size () + "\">" );
	                s.append ( "<b>" + this.getOtherShortAnswerCount () + "</b>" );
	                if ( this.getOtherShortAnswerCount () > 0  )
	                  s.append ( "</a>" );
	              }
	              s.append ( "</td>\n");
	              if ( !survey.singleEntry ) {
	                s.append ( "<td align=\"right\">\n" ); // end: class=\"highlightresponses\"

	                int percent = 0;
	                if ( survey.getNUMENTRIES()  > 0 )
	                  percent = Math.round ( (float) this.getOtherShortAnswerCount() / (float) survey.getNUMENTRIES()  * 100 );
	                s.append ( " (" );
	                if ( percent < 10 ) { s.append("&nbsp;"); }
	                s.append ( Integer.toString(percent) + "%)" );
	                s.append ( "</td>\n");

	                // output bar chart
	                s.append ( "<td align=\"left\">\n" );
	                if ( percent > 0 ) {
	                  s.append ( "<img src=\"/common/img/resultsbar.gif\" style='height: 10px;width:"+percent+"px;' height=\"10\" width=\""+percent+"\">" );
	                }
	                s.append ( "</td>\n");
	              }

	              s.append ( "</tr>\n" );
	            } // end: if ( mode == SurveyEntryForm.modeResultsSummary &&...

	            if ( mode == iSurveyEntryForm.modeResultsSummary && this.getClass().getName().equals("edu.vt.ward.survey.InputRadio") && !survey.singleEntry ) {
	              s.append ( "<tr>\n" );
	              s.append ( "<td>\n" );
	              s.append ( "<i>"+"no answer"+"</i>" );
	              s.append ( "</td>\n" );
	              s.append ( "<td align=\"right\">\n" ); // end: class=\"highlightresponses\"
	              s.append ( "<b>" );
	              int totalAnswers = 0;
	              for ( int i = 0; i < options.size(); i++ ) {
	                totalAnswers += ((Option) options.get(i)).getCount ();
	              }
	              totalAnswers += this.getOtherShortAnswerCount ();
	              long numNoAnswer = survey.getNUMENTRIES() - totalAnswers;
	              s.append( numNoAnswer );
	              s.append ( "</b>" );
	              s.append ( "</td>\n" );
	              s.append ( "<td align=\"right\">\n" ); // end: class=\"highlightresponses\"
	              int percent = 0;
	              if ( survey.getNUMENTRIES()> 0 )
	                percent = Math.round ( (float) numNoAnswer / (float) survey.getNUMENTRIES() * 100 );
	              s.append ( " (" );
	              if ( percent < 10 ) { s.append("&nbsp;"); }
	              s.append ( Integer.toString(percent) + "%)" );
	              s.append ( "</td>\n" );

	              // output bar chart
	              s.append ( "<td align=\"left\">\n" );
	              if ( percent > 0 ) {
	                s.append ( "<img src=\"/common/img/resultsbar.gif\" style='height: 10px;width: "+percent+"px;' height=\"10\" width=\""+percent+"\">" );
	              }
	              s.append ( "</td>\n");

	              s.append ( "</tr>\n" );
	            } // end: if ( mode == SurveyEntryForm.modeResultsSummary &&...

	            if ( mode == iSurveyEntryForm.modeResultsSummary  ) {
	              s.append ( "</table>\n" );
	            }

	    
	    }
	    
	    return s.toString();
  }
  
 
 
  public String getEditFormHTML_New ( int sectionNr, int questionNr, String type,String above, String inputErrorId ) {
	    StringBuffer s = new StringBuffer ();

	    s.append ( super.getEditFormBeginHTML_New ( sectionNr, questionNr, type,above ) );
	    if ( inputErrorId != null && inputErrorId.equals ( "invalidOptions" ) ) {
	      s.append ( "<br><font color=\"#ff0000\"><b>"+"Error!"+"</b></font> "+"Please enter at least"+" " + this.getMinOptions () + " " );
	      if ( this.getMinOptions () > 1 )
	        s.append ( "options" );
	      else
	        s.append ( "option" );
	      s.append ( ".<br>" );
	    }
	    s.append ( "<b>"+"Multiple choice options:"+"</b> <font color=\"#999999\">("+"list one per line, minimum is"+" " + this.getMinOptions () + ")</font><br>\n" );
	    s.append ( "<textarea name=\"options\" wrap=\"virtual\" cols=\"60\" class='span12' rows=\"5\">" );

	    if ( options.size() > 0 ) {
	      for ( int i = 0; i < options.size(); i++ ) {
	        s.append ( HTMLUtils.encode ( ((Option) options.get(i)).getLabel () ) + "\n" );
	      }
	    }
	    else {
	      s.append("option 1"+"\n"+"option 2"+"\n"+"option 3");
	    }
	    s.append ( "</textarea><br>\n" );

	    s.append ( "<br><b>"+"Do you want an &amp;quot;other:&amp;quot; short answer field?"+"</b><br>\n" );

	    s.append ( "<div class='control-group'>");
   	    s.append ( "<div class='controls'>");
   	    s.append ( "<label>");
	    s.append ( "<input type=\"radio\" name=\"otherShortAnswer\" value=\"yes\"" );
	    if ( this.getOtherShortAnswerLabel () != null )
	      s.append ( " checked" );
	    s.append ( ">\n" );
	    s.append ( "<span class='lbl'>"+"Yes, and use this label text:"+"</span>");
	    s.append ( "<input type=\"text\" size=\"30\" maxLength=\"100\" name=\"otherShortAnswerLabel\" value=\"" );
	    if ( this.getOtherShortAnswerLabel () != null )
	      s.append ( this.getOtherShortAnswerLabel () );
	    else
	      s.append ( "other:" );
	    s.append ( "\">\n" );
	    s.append ( "</label>");
	    s.append ( "<label>");
	    s.append ( "<input type=\"radio\" name=\"otherShortAnswer\" value=\"no\"" );
	    if ( this.getOtherShortAnswerLabel () == null )
	      s.append ( " checked" );
	    s.append ( ">\n" );
	    s.append ( "<span class='lbl'>"+"No"+"</span>");
	    s.append ( "</label>");
	    s.append ( "</div>");
	    s.append ( "</div>");

	    s.append ( "<br>\n" );

	    s.append ( "<b>"+"Layout:"+"</b><br>\n" );
	    
	    s.append ( "<div class='control-group'>");
   	    s.append ( "<div class='controls'>");
   	    s.append ( "<label>");
	    s.append ( "<input type=\"radio\" name=\"layout\" value=\"vertical\"" );
	    if ( this.getLayout () == null || this.getLayout ().equals ( "vertical" ) )
	      s.append ( " checked" );
	    s.append ( ">\n" );
	    s.append ( "<span class='lbl'>"+"each option on a separate line"+"</span>");
	    s.append ( "</label>");
   	    s.append ( "<label>");
	    s.append ( "<input type=\"radio\" name=\"layout\" value=\"horizontalTextRight\"" );
	    if ( this.getLayout () != null && this.getLayout ().equals ( "horizontalTextRight" ) )
	      s.append ( " checked" );
	    s.append ( ">\n" );
	    s.append ( "<span class='lbl'>"+"all options on one line"+"</span>");
	    s.append ( "</label>");
	    s.append ( "</div>");
	    s.append ( "</div>");

	    s.append ( "<br><br>\n" );

	    s.append ( super.getEditFormEndHTML_New () );

	    return s.toString ();
  }

  public String makeEditFormChanges ( HttpServletRequest request ) {
    super.makeEditFormChanges ( request );

    options = new Vector ();

    try {
      int numOptions = 0;
      StringTokenizer newOptions = new StringTokenizer( (String) request.getParameter ( "options" ), "\n");
      while (newOptions.hasMoreTokens()) {
        Option newOption = new Option ( newOptions.nextToken().trim() );
        if ( !newOption.getLabel().equals("") ) {
          this.addOption ( newOption );
          numOptions++;
        }
      }

      if ( numOptions < this.getMinOptions () )
        throw new Exception ();
    }
    catch ( Exception e ) { // user input error occurred
      return "invalidOptions";
    }

    try {
      String layout = (String) request.getParameter ( "layout" );
      if ( layout == null ) {
        throw new Exception ();
      }
      else if ( layout.equals ( "vertical" ) ) {
        this.setLayout ( "vertical" );
      }
      else if ( layout.equals ( "horizontalTextRight" ) ) {
        this.setLayout ( "horizontalTextRight" );
      }
      else if ( layout.equals ( "horizontalTextAbove" ) ) {
        this.setLayout ( "horizontalTextAbove" );
      }
      else if ( layout.equals ( "horizontalTextBelow" ) ) {
        this.setLayout ( "horizontalTextBelow" );
      }
      else {
        throw new Exception ();
      }
    }
    catch ( Exception e ) { // user input error occurred
      return "invalidLayout"; // not used
    }

    try {
      String otherShortAnswer = (String) request.getParameter ( "otherShortAnswer" );
      if ( otherShortAnswer == null ) {
        throw new Exception ();
      }
      else if ( otherShortAnswer.equals ( "yes" ) ) {
        String otherShortAnswerLabel = (String) request.getParameter ( "otherShortAnswerLabel" );
        if ( otherShortAnswerLabel == null ) {
          throw new Exception ();
        }
        else {
          this.setOtherShortAnswerLabel ( otherShortAnswerLabel );
        }
      }
      else if ( otherShortAnswer.equals ( "no" ) ) {
        this.setOtherShortAnswerLabel ( null );
      }
      else {
        throw new Exception ();
      }
    }
    catch ( Exception e ) { // user input error occurred
      return "invalidOtherShortAnswer"; // not used
    }
   return  null; // no user input error occurred
  }

  public TbQuestion getTbQuestion ( int mode ) {
	  	TbQuestion inputOptionList = super.getTbQuestion(mode);
	  	if ( mode == iSurveyEntryForm.modeEdit || mode == iSurveyEntryForm.modeResultsSummary )	inputOptionList.setQUESTION_LAYOUT(HTMLUtils.xmlFilter(this.getLayout ()));

    	ArrayList<TbQuestionLabel> aQuestionLabels = new ArrayList<TbQuestionLabel>();
	    for ( int i = 0; i < options.size(); i++ ) {
	    	
	    	TbQuestionLabel option = new TbQuestionLabel(); 
	        if ( mode == iSurveyEntryForm.modeEdit || mode == iSurveyEntryForm.modeResultsSummary )	option.setQUESTION_LABEL(HTMLUtils.xmlFilter(((Option) options.get(i)).getLabel()));
	        if ( mode == iSurveyEntryForm.modeEdit )	option.setEXPORTCODE(HTMLUtils.xmlFilter(((Option) options.get(i)).getExportCode()));	
	        if ( ((Option) options.get(i)).getSelected () ) option.setSELECTED("1");
	        option.setQUESTION_LABEL_ID(((Option) options.get(i)).getQuestionLableId());
	        
	        if ( mode == iSurveyEntryForm.modeResultsSummary ) {
	          option.setRESULT_CNT( ((Option) options.get(i)).getCount ());
	        }
	        aQuestionLabels.add(option);
	        
	    }
        inputOptionList.setQUESTIONLABELS(aQuestionLabels);
	    
	  	if ( mode == iSurveyEntryForm.modeEdit || mode == iSurveyEntryForm.modeResultsSummary ) {
	    	  if ( this.getOtherShortAnswerLabel () != null ) {
	    		  inputOptionList.setOTHERANSWER(HTMLUtils.xmlFilter(this.getOtherShortAnswerLabel ()));
		        if ( mode == iSurveyEntryForm.modeResultsSummary )  inputOptionList.setOTHERANSWER_RESULT_CNT (this.getOtherShortAnswerCount ());
	    	  }else if ( mode == iSurveyEntryForm.modeEntry ) {
		        if ( !this.getOtherShortAnswer ().equals ( "" ) ) {
		        	inputOptionList.setOTHERANSWER(HTMLUtils.xmlFilter(this.getOtherShortAnswer ()));	
		        }
	    	  }
	     }
	    
	  	return inputOptionList;
  	}
  
 
  public void updateResultsSummary ( Question entryQuestion ) {
    // iterate through every option and determine whether it has been selected
    for ( int i = 0; i < options.size(); i++ ) {
      Option entryOption = (Option) ((OptionList) entryQuestion).getOptions().get(i);
      ((Option) options.get(i)).updateResultsSummary ( entryOption );
    }
    if ( !((OptionList) entryQuestion).getOtherShortAnswer ().equals("") ) {
      this.otherShortAnswerCount++;
      this.setOtherShortAnswer(((OptionList) entryQuestion).getOtherShortAnswer ());
    }
  }

  public void updateResultsSummary (int sectionId, TbResults entryQuestion ) {
	 Option o = null;
	 if  (getOptions()!=null || getOptions().size()>0){
			//System.out.println("**************getOptions().size()"+getOptions().size()+"]********************************");
			//System.out.println("**************entryQuestion.getTbResultsLabels().size()"+entryQuestion.getTbResultsLabels().size()+"]********************************");

		for (int i=0;i<getOptions().size();i++){
			  o =(Option) getOptions().get(i);
			  for(TbResultsLabel aTbResultsLabels: entryQuestion.getTbResultsLabels()){
				  if(sectionId==aTbResultsLabels.getSECTION_ID() & this.getQuestionId()==aTbResultsLabels.getQUESTION_ID() && i==aTbResultsLabels.getQUESTION_LABEL_ID()){
					  if(aTbResultsLabels.getQUESTION_LABEL_RESULT().equals("1")){
						  //System.out.println("**************updateResultsSummary Selected["+aTbResultsLabels.getQUESTION_LABEL_ID()+"/"+aTbResultsLabels.getQUESTION_LABEL_RESULT()+"]********************************");
						  o.setCount(1);
						  o.setSelected(true);
					  }else{
						  o.setCount(0);
						  o.setSelected(false);
					  }
				  }
			  }
			  getOptions().set(i, o);
		}
	 }
	 this.setOtherShortAnswer(entryQuestion.getOTHERANSWER_RESULT());
	 /*
	    for ( int i = 0; i < options.size(); i++ ) {
	        Option entryOption = (Option) ((OptionList) entryQuestion).getOptions().get(i);
	        ((Option) options.get(i)).updateResultsSummary ( entryOption );
	      }
	      if ( !((OptionList) entryQuestion).getOtherShortAnswer ().equals("") ) {
	        this.setOtherShortAnswer(((OptionList) entryQuestion).getOtherShortAnswer ());
	      }
	      */
  }

  
  public String getResultsDetailsHTML ( Question entryQuestion ) {
    StringBuffer s = new StringBuffer ("");

    for ( int i = 0; i < options.size(); i++ ) {
      String optionText = "";
      Option entryOption = (Option) ((OptionList) entryQuestion).getOptions().get(i);
      if ( entryOption.getSelected () ) {
        s.append ( ((Option) options.get(i)).getLabel() + "\n");
      }
    }
    if ( !((OptionList) entryQuestion).getOtherShortAnswer ().equals("") ) {
      s.append ( this.getOtherShortAnswerLabel() + " " + ((OptionList) entryQuestion).getOtherShortAnswer () + "\n" );
    }


    return s.toString();
  }

  
  public String getExport ( Question entryQuestion, String delimiter ) {
    StringBuffer s = new StringBuffer ("");

    /*
    if ( this.getExportExpand() ) {
      for ( int i = 0; i < options.size(); i++ ) {
        Option entryOption = (Option) ((OptionList) entryQuestion).getOptions().get(i);

        if ( i > 0 ) { s.append ( delimiter ); }
        if ( entryOption.getSelected () ) { s.append ( "1" ); }
        else { s.append ( "0" ); }
      }

      if ( this.getOtherShortAnswerLabel () != null ) {
        s.append ( delimiter );
        s.append ( Config.translateExportDelimiters ( ((OptionList) entryQuestion).getOtherShortAnswer (), delimiter ) );
      }
    } // end: if ( this.exportExpand() )
    else {
      boolean firstSelected = true;
      for ( int i = 0; i < options.size(); i++ ) {
        Option entryOption = (Option) ((OptionList) entryQuestion).getOptions().get(i);

        if ( entryOption.getSelected () ) {
          if ( !firstSelected ) {
            if ( delimiter.equals(",")) { s.append("; "); } else { s.append(", "); }
          }
          firstSelected = false;
          //s.append ( Config.translateExportDelimiters ( ((Option) options.get(i)).getLabel (), delimiter ) );
          s.append ( Config.translateExportDelimiters (""+(i+1), delimiter));
        }
      }
      */
	    boolean firstSelected = true;
	    for ( int i = 0; i < options.size(); i++ ) {
	    	Option entryOption = (Option) ((OptionList) entryQuestion).getOptions().get(i);
	    	if ( entryOption.getSelected () ) {
	    		if ( !firstSelected ) {
	    			if ( delimiter.equals(",")) { s.append("; "); } else { s.append(", "); }
	        }
	        firstSelected = false;
	        s.append ( delimiter);
	    }
	    
	    if ( this.getOtherShortAnswerLabel () != null && ((OptionList) entryQuestion).getOtherShortAnswer () != null && !((OptionList) entryQuestion).getOtherShortAnswer().equals ("") ) {
	        if ( !firstSelected ) {
	          if ( delimiter.equals(",")) { s.append("; "); } else { s.append(", "); }
	        }
	        s.append (  this.getOtherShortAnswerLabel () + " "+ delimiter  );
	        s.append (  ((OptionList) entryQuestion).getOtherShortAnswer ()+ delimiter  );
	      }
	
	    } // end else: if ( this.exportExpand() )
	
	    return s.toString();
  }


  
  
}
