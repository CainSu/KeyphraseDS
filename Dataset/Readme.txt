This is the survey generation dataset created for experiments presented in the short paper titled "A System for Summarizing Scientific Topics Starting from Keywords" submitted to ACL for review.

The data consists of resources for 7 topics in NLP, listed below. For each topic, there is a directory containing three files:

- RDG_ID.resc.txt contains a metadata and description of each survey and tutorial used for extracting the factoids for the current topic.
- RDG_ID.facts.txt contains a list of the factoids extracted for the current topic. We only consider factoids appearing in more than one survey or tutorial for our experiments.
- RDG_ID.cit.ann.txt contains the input citing sentences annotated with facts from the above file by human judges. Each sentence is preceded by the factoid IDs that were judged to be in the sentence.

List of topics:

# Topic ID ::: Topic Name
------------------------------
  RDG01    ::: summarization
  RDG02    ::: question answering
  RDG03    ::: word sense disambiguation
  RDG04    ::: named entity recognition
  RDG05    ::: sentiment analysis
  RDG06    ::: semantic role labeling
  RDG07    ::: dependency parsing

------The following content is added for paper titiled "KeyphraseDS: Automatic Generation of Survey by Exploiting Keyphrase Information." ---------------------
- RDG.cit.ann.txt.processed		contains the input citing sentences filtered out factoids and paper IDs.
- RDG.cit.ann_factoids			contains the input citing sentences preceded by the factoid IDs.
- vocabulary.txt				contains the whole vocabulary derived from the citing sentences.
- phrase.txt					contains the whole extracted keyphrase list through CRF model.
- factoid.txt					contains the whole extracted factoids list formatted as: factoid ID	factoid weight	factoid content.
- featureVectorMatrix.txt		contains the whole feature matrix of citing sentences through one-hot representation with size |sentences|*|vocabulary|
- indexTermArray.txt			contains each sentence's term index.