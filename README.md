# Text-First: A sample Todo REST API

This repo serves as an example of my "Text-First" approach to AI-assisted software development.

## The Idea

When planning to implement any non-trivial feature with AI, I typically follow a simple process:

```mermaid
%%{init: {"themeCSS": ".edgePath .path, .flowchart-link { stroke: #c80 !important; } .arrowheadPath, marker path { fill: #c80 !important; stroke: #c80 !important; }"}}%%
flowchart LR
  discuss["1 Discuss"] --> design["2 Design"]
  design --> plan["3 Plan"]
  plan --> implement["4 Implement"]
  implement --> test["5 Test"]
  test --> refactor["6 Refactor"]

  classDef early fill:#1a1410,color:#ffb000,stroke:#ffb000;
  classDef late fill:#231c16,color:#ffb000,stroke:#ffb000;
  class discuss,design,plan early;
  class implement,test,refactor late;
```

The first three steps are the "Text-First" part. They are all about deriving an implementation
plan. I start by discussing and designing the feature with AI which yields documentation artefacts
that serve a dual purpose: they're the blueprint for implementation, and they become the reference
for future maintenance and evolution - consumed by humans and AI alike.

Once we have a clear plan (most tools offer a "Plan mode"), we can hand off to implementation ("Agent mode")
and let AI do the heavy lifting of writing code, tests and documentation. Modern LLMs are reasonably
good at verifying their own work, especially with clear test specifications. Therefore testing is
typically a mixture of the AI auto-correcting its own mistakes and adding manual tests that ensure
correctness and also serve as living documentation at the same time.

The final refactoring step is something often overlooked - regardless of AI assistance - but it's
crucial to keep the codebase clean and maintainable in the long run - current LLMs are not great at
grasping the big picture and often introduce redundancies and inconsistencies that need to be ironed
out by the developer.

## Why Text-First?

Hold on, you might say, that's yet another Spec-Driven Development (SDD) approach, isn't it?
Not quite. It shares the spec-first approach and many of its principles, but I refuse to introduce
yet another framework that forces me into abstraction lock-in.

After two decades in the industry, I've seen too many frameworks come and go. What happens when
your SDD framework of choice ceases to exist? You're left with a pile of specs that just lost their
meaning.

No, Text-First is a non-framework framework. It is simply a set of principles and best practices
to bring order and reproducibility to the chaos of creating and maintaining software with AI.

> Text-First: If it can be text, it should be text.

Lightweight, focused on the essentials, easy to extend and most importantly: not tied to a specific
tool or framework. It works with any LLM, can be used with GitHub Copilot, Claude Code, Codex, Cursor,
Antigravity, or whatever comes next. No tooling circus. No hocus-pocus. Just text.

> If SDD is the GitFlow of AI-assisted software development, then Text-First is its GitHub Flow.

## There Ain't No Such Thing As Free Tokens

Whatever you feed an LLM - PDF, Word, Excel, Visio diagrams or voice transcripts - it ends up as
text tokens anyway. So why pay the conversion tax instead of feeding it text in the first place?

Even as context windows grow, token cost remains a concern. Among human-readable formats, Markdown
has the highest information density per token you can get - which makes Text-First the most
token-efficient approach to spec-driven development available. 

## How to Use This Repo

The goal of this repo is to implement a simple Todo REST API using the Text-First approach. The
branches are meant to be read as stages of that process:

- `main` tells the story and explains the methodology.
- `bare-repo` is the initial state before the AI-assisted implementation starts.
- `implementation` contains one sample final state after running through the process.

The implementation is in Java using Spring Boot 4, and the `bare-repo` branch contains the initial
empty project that was derived from the [Spring Initializr](https://start.spring.io/) with the following
dependencies:

```gradle
dependencies {
  implementation 'org.springframework.boot:spring-boot-starter-webmvc'
  testImplementation 'org.springframework.boot:spring-boot-starter-webmvc-test'
  implementation 'org.springframework.boot:spring-boot-starter-validation'
  testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}
```

### Running Locally

You need Java 25. The project uses the Gradle Wrapper, so no separate Gradle installation is required.

Run the tests:

```bash
./gradlew test
```

Start the application on <http://localhost:8080>:

```bash
./gradlew bootRun
```

On the `bare-repo` branch, this starts the initial Spring Boot application before the Todo API has been
implemented. On the `implementation` branch, the same commands run the completed sample API and its tests.

### `AGENTS.md`: The Single Source of Truth
To avoid having to restate the same information over and over again in prompts, an [AGENTS.md](./AGENTS.md)
describing the project context, conventions and architecture serves as the single source of truth for all
implementation details. It is the "spec" that guides the implementation and maintenance of the API.

### `doc/todo-class-diagram.md` and `doc/api-endpoints.md`
These documents contain the authoritative API contract and the Todo resource model. They are the blueprints
for implementation and must be read and followed before making any non-trivial changes to the API. They also serve as living documentation that can be referred to by both humans and AI in the future. Similar to
the `README.md`, they start as placeholders in the `bare-repo` branch and are filled with actual content as the implementation progresses. More specifically, they are the output artifacts of the "Design" step in the Text-First process, and they are the reference for the "Plan" and "Implement" steps that follow.

### `create-endpoint` Skill: The Workflow for API Changes
Creating and modifying API endpoints is governed by a workflow described in the
[`create-endpoint`](.agents/skills/create-endpoint/SKILL.md) skill, which ensures that all changes are
properly documented and follow the established conventions.

### `create-test` Skill: Teaching LLMs How to Write Spring Boot 4 Tests
Spring Boot 4 is not well-represented in current LLM training data, so the
[`create-test`](.agents/skills/create-test/SKILL.md) skill teaches the model how to write
Spring Boot 4 tests by explaining the package layout and testing conventions. This usually results in one-shot
success instead of trial-and-error.

### Tool-agnostic Skills
Both Codex and GitHub Copilot support skills in the `.agents/skills` directory, but Claude still expects them in
the `.claude/skills` directory. To avoid duplication, we keep all skills in the `.agents/skills` directory and simply
symlink it to `.claude/skills`.

### `README.md`
On the `bare-repo` branch, the `README.md` is a stub with placeholder content that will be replaced with the
actual documentation as the implementation progresses. This is a contract documented in the `AGENTS.md` that
ensures that the README is always up-to-date and serves as a reliable source of information about the project.

On the `main` branch, the `README.md` contains the narrative overview you are currently reading 😃.
On the `implementation` branch, it should read like the README of a completed Todo API project, including
the generated endpoint overview and usage details.

### Implementing the API

Since we did most of the heavy lifting in `AGENTS.md` and the `create-endpoint` and `create-test` skills,
the prompts guiding the actual implementation are surprisingly simple and concise.

#### 1. Discuss

To simulate a typical `"Discuss"` step, we start with a simple prompt that just asks what a typical
Todo REST API should look like:

```text
What endpoints should a simple Todo API provide?
```

The result will already be guided by the conventions and architecture described in `AGENTS.md` and the `create-endpoint` and `create-test` skills. Think of these prompt customization files as the "guardrails" that keep the implementation on track and ensure that the output is consistent with the established conventions and architecture.

We deliberately stay in the same chat session to follow the "Generated Knowledge" prompting pattern, which allows
the LLM to build on the previous output and maintain context throughout the implementation process.

#### 2. Design

Entering the `"Design"` step, we now ask the LLM to create a Mermaid class diagram based on the API endpoints
that it just proposed:

```text
Propose a Mermaid class diagram for a simple Todo resource served by the simple Todo API.
```

Based on the non-deterministic output of the LLM, the proposed attributes of the Todo resource will typically
vary, and we'll have to iterate to achieve exactly the set of attributes we want. Iterate until the resulting
class diagram consists of a Todo class with the following attributes:

- id: Long
- title: String
- description: String
- completed: Boolean

You can use prompts such as the following to guide the iteration:

```text
Leave `createdAt` and `updatedAt` out for now.
```

Once everything looks good, we tell the agent to persist what we just designed in our stub documentation artifacts:

```text
- Add the Mermaid diagram to `doc/todo-class-diagram.md`.
- Add the API endpoints you proposed above to `doc/api-endpoints.md`.
```

#### 3. Plan

We have everything we need and can now enter the `"Plan"` step and let the agent figure out how to implement the API based on the documentation we just created. If your AI tool supports a "Plan mode", you may want to switch to it. The prompt for this step is as simple as:

```text
Plan the initial implementation of the Todo API.

Steps in implementation order.
```

Actually, the second line is optional, but it encourages the LLM to produce the implementation plan in a logical order
that can be followed step by step. If left out, LLMs have a tendency to group tasks by category (e.g. "First all controllers, then all services, then all tests") which is not how you would typically implement an API yourself.

Since the plan itself is a valuable piece of documentation that can be referred to in the future, we can either ask the agent
to persist it or simply copy and paste it ourselves, e.g. into a file such as `doc/implementation-plan.md`. This way, we have
a record of the implementation process that can be referred to in the future, both by humans and AI.

#### 4. Implement

Finally, we can enter the `"Implement"` step and let the agent do the heavy lifting of writing code, tests and 
documentation based on the plan we just created. Most AI tools offer a button to kick off implementation; otherwise,
just prompt the agent to start implementing:

```text
Start implementing the API based on the implementation plan you just created.
```

or just:

```text
Start implementation.
```

#### 5. Test

Since we made tests a mandatory part of the implementation process in the `AGENTS.md` and the `create-test` skill, the
resulting implementation will typically include tests that are not only ensuring correctness but also serve as living
documentation for the API. Additionally, the LLM will use the tests it generates to verify its own work and auto-correct
any mistakes it makes along the way, which results in a much smoother implementation process. No more copying stacktraces
back and forth between the IDE and the LLM, no more manual debugging.

Once the implementation is done, the LLM will typically tell you how to run the application and the tests, or you may have
a look at the `README.md` which should have been updated with the relevant information as part of the implementation process.

#### 6. Refactor

The final `"Refactor"` step is something that often gets overlooked in SDD frameworks (and elsewhere 😃), but it's crucial
to keep the codebase clean and maintainable in the long run. AI can assist with it. Just prompt it to refactor the
codebase based on the established conventions and architecture, and it will typically do a decent job at ironing out
any inconsistencies and redundancies that may have been introduced during implementation. Example prompts:

```text
Refactor the codebase to ensure consistency with
the established conventions and architecture.
```

or

```text
Identify duplicate or almost identical code and refactor it to honor the DRY principle.
```

or simply

```text
Do you see any potential for refactoring?
Please explain your reasoning and list them in order of importance.
```

## More Details

You can find more details in the [presentation](./text-first.make-ai-great-already.pdf) I gave at
[Gedoplan's Expertenkreis Java](https://www.gedoplan.de/expertenkreisjava) in April 2026.

Stay tuned for a more detailed write-up in my [blog](https://martinbosslet.dev/en/blog/) in the coming weeks
and/or follow [klautcode@Mastodon](https://mastodon.social/@klautcode),
[klautcode@Bluesky](https://bsky.app/profile/klautcode.bsky.social) or
[emboss@X](https://x.com/_emboss_) for updates.

## License

Released under the [MIT License](./LICENSE) — feel free to copy, adapt, and reuse anything in this repo
(including the Text-First approach itself) in your own projects.
