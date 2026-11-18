# Guild Registration System

## How to Run with Assertions Enabled
Compile all `.java` files and run with the `-ea` (enable assertions) flag:
```bash
javac *.java
java -ea GuildRegistration
```

---

## Call-Stack Trace for Negative Mission Count (`-2`)
When a user enters `-2` for the mission count:
1. **Method that throws the exception:** `validateMember()` checks `if (missions < 0)` and throws an `InvalidRegistrationException`.
2. **Method it passes through:** Because `registerMember()` declares `throws InvalidRegistrationException` and does not catch it locally, the exception propagates backward up the call stack through `registerMember()`. Note that execution in `registerMember()` stops immediately at the `validateMember()` call before `roster.add()` is ever reached.
3. **Method that catches it:** `runMenu()` catches the `InvalidRegistrationException` in its dedicated `catch (InvalidRegistrationException e)` block, increments the rejected attempt counter, and displays the validation error message to the user.
4. **When `finally` runs:** Immediately after the `catch` block finishes executing, the `finally` block in `runMenu()` runs and prints `"Registration attempt ended."` before returning to the main menu loop.
5. **Why the roster remains unchanged:** `roster.add(newMember)` is placed *after* `validateMember()` in `registerMember()`. When `validateMember()` throws the exception, `registerMember()` aborts immediately, so the invalid member is never added to the `ArrayList<GuildMember>`.

---

## Assertion vs. Exception Explanation
* **Why negative mission count uses an exception:** User input comes from outside the program and is expected to occasionally contain mistakes. Exceptions are designed to recover gracefully from external runtime errors, inform the user what went wrong, and keep the menu running.
* **Why roster-size verification uses an assertion:** Verifying `roster.size() == previousSize + 1` immediately after calling `roster.add()` checks an internal program invariant that should always be true if the code is written properly. Assertions are meant to catch internal programmer logic bugs during testing rather than handle user input errors.

---

## Recorded Test Results
Ran the 6 required attempts in a single session with assertions enabled (`java -ea GuildRegistration`):

| Attempt | Input | Actual Result |
| :--- | :--- | :--- |
| 1 | `Avery`, `Scout`, `4` | Accepted (`Successfully registered: Avery (Scout, Missions: 4)`). `finally` printed `"Registration attempt ended."` |
| 2 | `Casey`, `Scout`, `five` | Caught `NumberFormatException`. Printed `"Conversion error: Mission count must be a valid whole number."` and returned to menu. `finally` ran. |
| 3 | `Jordan`, `Ranger`, `-2` | Caught `InvalidRegistrationException`. Printed `"Validation error: Mission count cannot be negative."` and returned to menu. `finally` ran. |
| 4 | `Taylor`, `Wizard`, `3` | Caught `InvalidRegistrationException`. Printed `"Validation error: Unsupported rank..."` and returned to menu. `finally` ran. |
| 5 | *(Blank)*, `Healer`, `2` | Caught `InvalidRegistrationException`. Printed `"Validation error: Name cannot be blank."` and returned to menu. `finally` ran. |
| 6 | `Morgan`, `Ranger`, `5` | Accepted (`Successfully registered: Morgan (Ranger, Missions: 5)`). `finally` printed `"Registration attempt ended."` |

**Final Verification:**
* **Full Roster:** Listed `Avery (Scout, Missions: 4)` and `Morgan (Ranger, Missions: 5)`.
* **Advanced Assignments List:** Listed `Morgan (Ranger, Missions: 5)`.
* **Exit Summary:** Displayed `Successful attempts: 2` and `Rejected attempts: 4`.
