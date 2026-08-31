Behavioural / Leadership Questions:

1- Tell me about a time you improved reliability/availability.
In my current role, I led a major migration of our API gateway from springboot version 2 to version 3, moving from Netflix Zuul to Spring Cloud Gateway. This involved redesigning routing, filters, and rate limiting to improve performance and maintainability.
During the migration, I noticed issues with distributed tracing propagation across microservices, which made debugging difficult. I took the initiative to introduce Micrometer Tracing across our services, replacing the older setup, and standardized trace context propagation.
As a result, we significantly improved observability, reduced troubleshooting time, and aligned the platform with modern Spring standards.

I led the migration from Spring Sleuth to Micrometer Tracing across multiple services to improve observability and align with newer Spring versions. I documented the setup and helped the team roll it out smoothly.

2- Describe a challenging incident and how you resolved it.

In my previous company, Avaya, we were working on a Contact Center as a Service (CCaaS) solution, where we were refactoring an on-premise system to the cloud. The platform consisted of many microservices and had strict performance requirements.
Our target was to handle around 100k requests per hour. During performance testing, one of the load services kept crashing, and we could not immediately identify the cause since no clear logs were indicating the problem.
To investigate, I reproduced the same load scenario and performed application profiling. Through profiling, I discovered a memory leak caused by improper handling of certain objects in the code. After fixing the issue, we reran the performance tests, and the service handled the load successfully without crashing.


At Avaya, we also had a email connector responsible for handling email configurations, which listened to events and stored the configuration in a Kafka state store. At one point, we noticed that although events were being published to Kafka, the consumer was not processing them and appeared to be stuck. There were no clear logs indicating the root cause.
Restarting the service would temporarily resolve the issue, but after some time the consumer would stop processing messages again.
To investigate, I reproduced the scenario locally and analyzed the consumer behavior. I discovered that the consumer was executing a blocking operation, which caused the processing thread to get stuck and prevented further message consumption.
I resolved the issue by refactoring the blocking operation to run asynchronously, allowing the consumer to continue processing messages normally. After implementing the fix, the consumer worked reliably without getting stuck.

3- Tell me about mentoring or leading others.

✔ Code reviews
✔ Pair programming
✔ Leading design discussions
✔ Coaching juniors

4- How do you ensure quality/scalability in your systems?

Observability (APM, metrics, tracing)
Load testing (k6, Gatling)
Chaos testing
Feature flags / gradual rollout
Canary releases / blue-green


5- Give an example where cross-team collaboration was essential.

Buckaroo gateway
Notification service migration
Micrometer tracing rollout

I was working on migrating our notification-service logging to STDOUT to integrate with Elastic APM. As part of this work, I migrated the service logs to the ECS-compatible format.
The goal was to move from the old logging pipeline:
Logback → File → Logstash → Elastic
to the new pipeline:
Logback → STDOUT → Filebeat → Elastic.
After completing the migration, the logs initially appeared in Elastic, but after some time they stopped showing up. After investigating, I discovered that Filebeat was no longer ingesting the logs into Elastic.
I communicated the issue to the SRE team, explained the problem, and they checked the Filebeat configuration. After their investigation, the issue was resolved and the logs started flowing correctly again.


6- Can you describe your ideal work environment?

My ideal work environment is one that encourages collaboration, ownership, and continuous learning. I value working in a team where knowledge sharing is common, feedback is constructive, and everyone is aligned toward clear goals.

7- What does technical ownership mean to you?
To me, technical ownership means being responsible for a feature or system end-to-end—from design and implementation to monitoring and continuous improvement.

It’s not just about writing code, but ensuring quality, scalability, and reliability, while proactively identifying issues and driving improvements. It also includes clear communication, collaborating with the team, and making decisions that balance short-term delivery with long-term maintainability.



7- introduce yourself?
I’m a Software Engineer with over 9 years of experience building web and enterprise applications using Java. Currently, I work at Payconiq, a fintech company with three main domains: Consumer, Payments, and Merchants.
My team is responsible for the Consumer and Payments domains. On the Consumer side, we handle services such as onboarding, notifications, KYC, and risk analysis. On the Payments side, we manage the full payment lifecycle—from creation and processing to refunds, payouts, and SEPA Credit Transfer (SCT) integrations with banks.



disaggrement sutation
mentor and coaching
which things you should focus first on if you have tight due dates
how you comunicate the esitamations if there is a forece to deliver fast with po
if you see something wrong from one of the team or PO side how you will act
how you solve a conflicat


***EPI***
EPI is building a unified European payment system with their wallet Wero.

Think of it as Europe’s answer to:
PayPal
Apple Pay
Google Pay

But bank-backed and pan-European.

They focus on:

Instant payments
P2P transfers
Wallet-based payments
Cross-country interoperability

This is very similar to payment platforms like Adyen or Stripe in terms of architecture complexity.

8- What do you think your experience level is and why? *

I consider myself a mid-to-senior level backend engineer. I have several years of experience building and maintaining scalable systems, working with APIs, databases, and cloud environments in production.

Beyond coding, I focus on writing clean, maintainable code, improving system performance, and contributing to architecture decisions. I also actively collaborate with teammates through code reviews and knowledge sharing.

Overall, I’m confident in independently delivering features end-to-end while continuously improving and learning.


What are your goals for professional development? 

My goal is to continue growing as a senior engineer by deepening my expertise in building scalable, distributed systems and taking more ownership in architectural decisions. I’m particularly interested in working more closely with data-intensive and AI-driven systems.

I also want to improve my impact beyond coding—contributing to product decisions, mentoring others, and driving best practices around reliability and performance. Over time, I aim to evolve into a technical leadership role where I can help shape both systems and teams.


taken 104 hours = 108/8=13 days
in a year I have 30 days which equal 30/12=2,5 vacation per month
in 8 months I have 2.5*8=20
