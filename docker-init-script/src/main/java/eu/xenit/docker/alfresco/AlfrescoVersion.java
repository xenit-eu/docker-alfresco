package eu.xenit.docker.alfresco;

public class AlfrescoVersion implements Comparable<AlfrescoVersion> {

    public final int major;
    public final int minor;
    private final String revision;

    public static AlfrescoVersion parse(String version) {
        String[] components = version.split(".", 3);
        return new AlfrescoVersion(components[0], components[1], components[2]);
    }

    public AlfrescoVersion(String major, String minor, String revision) {
        this.major = Integer.parseInt(major);
        this.minor = Integer.parseInt(minor);
        this.revision = revision;
    }

    public boolean isLessThan(String other) {
        return isLessThan(parse(other));
    }

    public boolean isLessThan(AlfrescoVersion other) {
        return compareTo(other) < 0;
    }

    public boolean isLessThanOrEqual(String other) {
        return isLessThanOrEqual(parse(other));
    }

    public boolean isLessThanOrEqual(AlfrescoVersion other) {
        return compareTo(other) <= 0;
    }

    public boolean isGreaterThan(String other) {
        return isGreaterThan(parse(other));
    }

    public boolean isGreaterThan(AlfrescoVersion other) {
        return compareTo(other) > 0;
    }

    public boolean isGreaterThanOrEqual(String other) {
        return isGreaterThanOrEqual(parse(other));
    }

    public boolean isGreaterThanOrEqual(AlfrescoVersion other) {
        return compareTo(other) >= 0;
    }

    public boolean isBetweenInclusive(String lower, String higher) {
        return isBetweenInclusive(parse(lower), parse(higher));
    }

    public boolean isBetweenInclusive(AlfrescoVersion lower, AlfrescoVersion higher) {
        if (equals(lower) || equals(higher)) {
            return true;
        }
        return isBetween(lower, higher);
    }

    public boolean isBetween(String lower, String higher) {
        return isBetween(parse(lower), parse(higher));
    }

    public boolean isBetween(AlfrescoVersion lower, AlfrescoVersion higher) {
        return isGreaterThan(lower) && isLessThan(higher);
    }

    @Override
    public int compareTo(AlfrescoVersion other) {
        if (major != other.major) {
            return Integer.compare(major, other.major);
        }
        if (minor != other.minor) {
            return Integer.compare(minor, other.minor);
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AlfrescoVersion that = (AlfrescoVersion) o;

        if (major != that.major) {
            return false;
        }
        if (minor != that.minor) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = major;
        result = 31 * result + minor;
        result = 31 * result + (revision != null ? revision.hashCode() : 0);
        return result;
    }
}
