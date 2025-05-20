© 2025 All Rights Reserved

# AICommunicator: Platform to communicate with public bots on different channels.

<br /> <br />
# System components

JobInterface: Entry point for the UI. Implementation determines the jobs to be scheduled
------------------------------------------------------------------------------------------
bool askQuestion(long requestId, string question) <br />
void registerAI(long requestId, AI ai) <br />
List<AIAnswers> getAnswersForQuestion(long requestId, long questionId) <br />


JobSubsystem: Invoked by JobInterface to schedule/cancel/query job(s)
-----------------------------------------------------------------------
void scheduleJob(Job job) <br />
void cancelJob(long jobId) <br />
List<Job> getJobsForRequest(long requestId) <br />

EventMessage Queue: Handle the new messages in the system
-----------------------------------------------------------

Jobs Dictionary: Handle the jobs currently active in the system
-----------------------------------------------------------------

EventMessage Consumer: Processes the new messages from the EventMessage Q
----------------------------------------------------------------------------

Data Model
------------
AI <br />
Address <br />
Version <br />
Public Key <br />
Priority <br />
Channels <br />

QuestionAnswer <br />
String question <br />
List<String, String> AIAnswers


