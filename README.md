# Collaborative Text Editor

Functionalities:
- Multiple users can edit the same document in real-time.
- A user can create/delete/update a document. 
- A user can send an invite to another user for a document
- The document is saved in the MongoDB database.

Details
- Operational Transformation (OT) is used to manage concurrent changes from multiple users

- [Visualization](https://operational-transformation.github.io/) of OT with a central server

Key Observation
- The source of truth is established by applying the deltas in the order they are received by the server.
- The client waits for the acknowledgment of a delta before sending the next delta.
- Before receiving acknowledgment of the sent delta, the client transforms all incoming deltas with respect to the sent delta.
- Transformed deltas are applied to the content.

