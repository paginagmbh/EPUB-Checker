#!/usr/bin/env python3
"""Fix an issue when unpacking the jar on a case insensitive file system.

When signing the mac app on a case sensitive file system, an error occurs, since
the jar contains both a text file named `LICENSE` at the root level as well as a
directory named `license/`. Some macOS installations are case insensitive, which
means they cannot differentiate between the two when given a path. The runner on
GitHub that is configured to execute the macOS notarization tasks is among them.
This results in a warning when unpacking the .jar in the process, which causes a
non-zero exit-code and thus maven failing. Even if the warning would be muted in
some way, the license file would be lost, which is undesirable. This outcome can
be prevented by renaming the license file inside the zip, which is this script's
goal.
    As far as I could find, Python does not allow for directly renaming anything
inside a zip file. The solution here is to copy every file inside the zip into a
zip file inside a temporary directory. The license file is then being renamed to
*LICENSE.txt* instead of *LICENSE* when placing it in the new zip file. Once the
process is complete, the new zip file replaces the old one. As a final step, the
temporary directory the zip file was stored in is deleted. Be aware, that even a
crash during the transfer will not prevent the directory from being deleted. For
debugging, delete the last line.
"""

import argparse
import shutil
import tempfile
import zipfile
from pathlib import Path


def main() -> None:
    """Execute the trans-zipping dance."""

    # Require one single command line argument, crash if it does not exist.
    parser = argparse.ArgumentParser()
    parser.add_argument("jar", help="the .jar file to rename the license in.")
    args = parser.parse_args()

    # Create a temporary directory to store the new jar file in. The Python docs
    # state, that I need to clean this up after.
    temp_dir = Path(tempfile.mkdtemp())
    # The original jar to rename the file in.
    jar = Path(args.jar)
    try:
        # The name of the temporary .jar file inside the temporary directory.
        temp_jar = temp_dir / jar.name
        # Open the jar that the data is read from.
        with zipfile.ZipFile(jar, "r") as in_file:
            # Open the jar that the data is written to.
            with zipfile.ZipFile(temp_jar, "w") as out_file:
                # Iterate over all the files inside the jar and copy them to the
                # new jar. Rename the license in the process.
                for item in in_file.infolist():
                    # Store the data of that file for writing into the new file.
                    # It is not used as a direct argument in the `writestr`, due
                    # to me changing the `item` object, which then would cause a
                    # crash. I change the data directly as it keeps all metadata
                    # stored inside ZipInfo intact.
                    data = in_file.read(item.filename)
                    # Filter any file at root level named `LICENSE` and edit its
                    # metadata to rename it to `LICENSE.txt`. `filename` doesn't
                    # in fact describe the file name but instead the path inside
                    # the zip file. So the `LICENSE` file inside `license/`, has
                    # the "filename" `license/LICENSE`.
                    if item.filename == "LICENSE":
                        item.filename = "LICENSE.txt"
                    # Add the file to the new jar, with the tweaked metadata.
                    out_file.writestr(item, data)
        # Replace the old jar with the filtered one.
        shutil.move(temp_jar, jar)
    finally:
        # Delete the temporary directory, even if an error occurred.
        shutil.rmtree(temp_dir)


if __name__ == "__main__":
    main()
