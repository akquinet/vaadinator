package org.vergien.vaadinator.model;

import de.akquinet.engineering.vaadinator.annotations.DisplayEnum;

public enum Anreden {
	HERR, FRAU, @DisplayEnum(captionText = "Fräulein")
	FROLLEIN
}
