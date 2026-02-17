package edu.vt.ward.survey;

import java.util.*;

import com.whomade.kycarrots.mgt.survey.vo.TbQuestion;
import com.whomade.kycarrots.mgt.survey.vo.TbResults;
import com.whomade.kycarrots.mgt.survey.vo.TbSection;
import com.whomade.kycarrots.mgt.survey.vo.TbSurvey;
import jakarta.servlet.http.HttpServletRequest;

public class iSection implements Cloneable {
  String title = "";
  iSurveyEntryForm surveyEntryForm = null; // keeps a reference back to the survey form it belongs to
  Vector questions = new Vector();

  private int sectionId = 0; // contains question text etc.
  
  
  //public Section () {
  //}

  public iSection ( iSurveyEntryForm surveyEntryForm ) {
    this.surveyEntryForm = surveyEntryForm;
  }

  public iSection ( iSurveyEntryForm surveyEntryForm, TbSection sourceElement ) {
    this.surveyEntryForm = surveyEntryForm;

    try { this.setSectionId ( sourceElement.getSECTION_ID()); }
    catch ( Exception e ) { }

    try { this.setTitle ( sourceElement.getSECTION_TITLE() ); }
    catch ( Exception e ) { }

    List<TbQuestion> questionElems = sourceElement.getQUESTIONS();
    
    for(TbQuestion questionElem : questionElems){
        if ( questionElem.getQUESTION_TYPE().equals( "inputRadio" ) ) {
            this.addQuestion ( new InputRadio (surveyEntryForm.getId(), questionElem ) );
          } else if ( questionElem.getQUESTION_TYPE().equals( "inputCheckbox" ) ) {
            this.addQuestion ( new InputCheckbox (surveyEntryForm.getId(), questionElem ) );
          } else if ( questionElem.getQUESTION_TYPE().equals( "inputTextline" ) ) {
            this.addQuestion ( new InputTextline (surveyEntryForm.getId(), questionElem ) );
          } else if ( questionElem.getQUESTION_TYPE().equals( "inputTextarea" ) ) {
            this.addQuestion ( new InputTextarea (surveyEntryForm.getId(), questionElem ) );
          } else if ( questionElem.getQUESTION_TYPE().equals( "inputComment" ) ) {
            this.addQuestion ( new InputComment (surveyEntryForm.getId(), questionElem ) );
          } else if ( questionElem.getQUESTION_TYPE().equals( "inputImage" ) ) {
              this.addQuestion ( new InputImage (surveyEntryForm.getId(), questionElem ) );
          } else if ( questionElem.getQUESTION_TYPE().equals( "inputLocation" ) ) {
              this.addQuestion ( new InputLocation (surveyEntryForm.getId(), questionElem ) );
          }
    }
  }

  public iSection ( iSurveyEntryForm surveyEntryForm, String title ) {
    this.surveyEntryForm = surveyEntryForm;
    this.title = new String (title);
  }

  public String getTitle () {
	  return this.title;
  }

  public void setTitle ( String title ) {
    this.title = title;
  }

  public iSection copy () {
    try {
      iSection copyOfSection = (iSection) super.clone ();				// return the clone
      copyOfSection.questions = new Vector();
      for ( int i = 0; i < questions.size (); i++ ) { // clone the questions the vector is pointing at
         Question copyOfquestion = getQuestion ( i ).copy ();
         copyOfSection.addQuestion ( copyOfquestion );
      }
      return copyOfSection;

    } catch ( CloneNotSupportedException e ) {
       throw new InternalError();
    }
  }

  public void addQuestion ( Question question ) {
    questions.add( question );
  }

  public void addQuestion ( int index, Question question ) {
    questions.add( index, question );
  }

  public void removeQuestion ( int index ) {
    questions.remove ( index );
  }

  public Question getQuestion ( int questionNr ) {
    return this.getQuestion ( questionNr, "" );
  }

  
  public Question getQuestion ( int questionNr, String type ) {
	    if ( type == null || type.equals("") ) {
	      return (Question) this.questions.get ( questionNr );
	    }
	    else {
	      return Question.getNewQuestion ( this.surveyEntryForm.getId(), type );
	    }
	  }

  public Question getQuestion ( int questionNr, String type, TbQuestion tbQuestion ) {
	    if ( type == null || type.equals("") ) {
				if ( tbQuestion.getQUESTION_TYPE().equals ( "inputComment" ) ) {
		    		type="comment";
			    }
			    else if ( tbQuestion.getQUESTION_TYPE().equals ( "inputRadio" ) ) {
		    		type="radio";
			    }
			    else if ( tbQuestion.getQUESTION_TYPE().equals ( "inputCheckbox" ) ) {
		    		type="checkbox";
			    }
			    else if ( tbQuestion.getQUESTION_TYPE().equals ( "inputTextline" ) ) {
		    		type="textline";
			    }
			    else if ( tbQuestion.getQUESTION_TYPE().equals ( "inputTextarea" ) ) {
		    		type="textarea";
			    }
			    else if ( tbQuestion.getQUESTION_TYPE().equals ( "inputImage" ) ) {
		    		type="textimage";
			    }
			    else if ( tbQuestion.getQUESTION_TYPE().equals ( "inputLocation" ) ) {
		    		type="textlocation";
			    }
	      return Question.getNewQuestion ( this.surveyEntryForm.getId(), type,tbQuestion );
	    }
	    else {
	      return Question.getNewQuestion ( this.surveyEntryForm.getId(), type );
	    }
	}

  public int getNumQuestions () {
    return questions.size ();
  }

  public String getHTML ( TbSurvey survey, int mode, int sectionNr ) {
    StringBuffer s = new StringBuffer ();
    /*
    s.append ( "<div class=\"row-fluid\">");
    s.append ( "<div class=\"span12 label label-large label-info arrowed-in arrowed-right\">");
    s.append ( "<b>"+this.getTitle()+"</b>");
    s.append ( "</div>");
    s.append ( "</div>");
    */
    if ( mode == iSurveyEntryForm.modeEdit ) {
        s.append ( "<a name=\"s_" + sectionNr + "\"></a>" );
      }

      s.append ("<br>"+this.getTitle());

      for ( int i = 0; i < this.getNumQuestions (); i++ ) {
       
      	if ( ( mode == iSurveyEntryForm.modeEntry || mode == iSurveyEntryForm.modePreview || mode == iSurveyEntryForm.modeResultsSummary )
             && i > 0 ) {
          if ( this.getQuestion ( i ).getShowDivider () ) {
            s.append ( this.surveyEntryForm.getDivider() );
          }
        }

        s.append ( this.getQuestion ( i ).getHTML ( survey, mode, sectionNr, i ) );

      }
      
      if ( mode == iSurveyEntryForm.modeEdit ) { // && this.getNumQuestions () > 0 ) {
        s.append ( "<br>" + Question.getAddButtonsHTML (this.surveyEntryForm.getId(), sectionNr, questions.size () ) + "<br>" );
      }

      return s.toString();
  }

  public String getExport ( Question entryQuestion, String delimiter ) {
    StringBuffer s = new StringBuffer ();

    for ( int i = 0; i < this.getNumQuestions (); i++ ) {
      s.append ( delimiter );
      s.append ( this.getQuestion ( i ).getExport ( entryQuestion, delimiter ) );
    }

    return s.toString();
  }

 

  public void makeEditFormChanges ( HttpServletRequest request ) {
    try {
      this.setTitle ( (String) request.getParameter ( "sectiontitle" ) );
    }
    catch ( Exception e ) { // response to user input error
    }
  }

  public String makeEntryChanges ( int sectionNr, HttpServletRequest request ) {
	  int iQuestionId=0;
	  		  
	  try {
      for ( int i = 0; i < this.getNumQuestions (); i++ ) {
    	iQuestionId   =  this.getQuestion ( i ).getQuestionId();
    	
        String errorId = ((Question) this.getQuestion ( i )).makeEntryChanges ( sectionNr, iQuestionId, request );
        if ( errorId != null )
          throw new Exception ();
      }
    }
    catch ( Exception e ) { // response to user input error
      return "error";
    }

    return null; // no input error occurred
  }

  public void updateResultsSummary ( iSection entrySection ) {
    for ( int i = 0; i < this.getNumQuestions (); i++ ) {
      // this works because the entry XML is supposed to have the exact same structure as the surveyEntryForm
      this.getQuestion ( i ).updateResultsSummary ( entrySection.getQuestion ( i ) );
    }
  }

  public void updateResultsSummary (int sectionId, ArrayList<TbResults>tbResults) {
	  System.out.println("updateResultsSummary this.getNumQuestions ()["+this.getNumQuestions ()+"]");
	  TbResults aTbResults=null;
	  int questionId=0;
	    for ( int i = 0; i < this.getNumQuestions (); i++ ) {
	    	questionId=getQuestion(i).getQuestionId();
	      // this works because the entry XML is supposed to have the exact same structure as the surveyEntryForm
	    	for(int j=0;j<tbResults.size();j++){
	    		aTbResults=tbResults.get(j);
	    	    if(sectionId==aTbResults.getSECTION_ID() && questionId==aTbResults.getQUESTION_ID()){
	    	    	this.getQuestion ( i ).updateResultsSummary (sectionId, aTbResults );
	    	    }
	    	}
	    }
	  }

public int getSectionId() {
	return sectionId;
}

public void setSectionId(int sectionId) {
	this.sectionId = sectionId;
}

}
