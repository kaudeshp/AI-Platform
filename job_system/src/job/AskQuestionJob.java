package job;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import request.Request;

import javax.xml.bind.ValidationException;

import dbclient.DocumentClient;
import entities.AI;
import entities.Question;
import message.AIResponseMessage;

public class AskQuestionJob extends GenericJob {
	
	AskQuestionJob(
			JobProcessor jobProcessor,
			long requestId,
			Question question) {
		
		super(jobProcessor, JobType.ASK_QUESTION, requestId);
		myQuestion_ = question;
	}
	
	AskQuestionJob(
			long requestId, Question question) {
		
		super(JobType.ASK_QUESTION, requestId);
		myQuestion_ = question;
	}
	
	AskQuestionJob(long requestId) {
		super(JobType.ASK_QUESTION, requestId);
	}
	
	private String QUESTION_KEY = "QUESTION_KEY";
	private Question myQuestion_;
	
	
	@Override
	protected void createInitialResources() {
		
		// TODO Auto-generated method stub
	}
	
	@Override
    protected void createParameters() {
		
		JobParameter questionParam =
				new JobParameter(
						0L,
						this.id_,
						ParameterType.Question,
						"Question_Value");
		
		this.parameters_.put(QUESTION_KEY, questionParam);
	}
	
	/**
     * validateParameters
     */
    @Override
    public void validateParameters()
            throws ValidationException {

        this.getLogger().fine(".validateParameters, validate parameters for "
                + this.toString());

        super.validateParameters();
        
        this.validateQuestion();
    }
	
	/**
     * Start job
     * @return List<Job>
     */
    @Override
    public ExecutionResult startJobBasic() {

    	ExecutionResult execResult = new ExecutionResult();	//Default - false
    	
    	List<GenericJob> childJobs = new ArrayList<GenericJob>();
    	
        super.startJobBasic();

	    this.getLogger().fine(".startJobBasic starting " + this.toString());        
        
        //TODO - Move this to a singleton
        DocumentClient documentClient = new DocumentClient();
        
        Question question = this.getMyQuestion();
        
        if(question.text.equalsIgnoreCase("NewsChannel")) {
        	
        	question.channels.clear();
        	question.channels.add("FinancialNewsAI");	//This changes the relevant AIs for the channels
        }
        
        try {
        	
	        List<AI> relevantAIs = documentClient.getAIForChannels(question.channels);
	        
	        for(AI ai : relevantAIs) {
	        	
	        	this.getLogger().fine(" Creating AskAI job for: " + ai.toString() + this.toString());
	        	
				GenericJob job = new AskSingleAIJob(this.jobProcessor_, 0, ai, question);
				
				this.addJobAsMyChild(job);
				childJobs.add(job);
				
				this.getLogger().fine(
	                    "Created AskAI job: " + job.id_ + " for ai Id: " + ai.id
	                );
			}
	        
        }
        catch(Exception e) {
        	
        	this.getLogger().finest(" Could not create child ask AI jobs " + this.toString());
        }
        
        //Passed all the previous steps
        execResult = new ExecutionResult(childJobs);
        this.state_ = JobState.WAITING_FOR_SUBJOB;
        
        if(execResult.success_) {
        	
        	this.state_ = JobState.WAITING_FOR_SUBJOB;
        	this.min_ = WAITING_FOR_SUBJOB_RESPONSE_MIN;
        	this.max_ = WAITING_FOR_SUBJOB_RESPONSE_MAX;
        }
        
        return execResult;
    }

    protected void validateQuestion() throws ValidationException {
    	
    	String Question = (String)this.parameters_.get(QUESTION_KEY).getValue();
    	
    	//TODO - Add validation code here
    }
    
    protected Question getMyQuestion() {
    	
    	return myQuestion_;
    }
}

