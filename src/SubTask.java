public class SubTask extends Task {
    public int parentId;

    public SubTask(int id, String name, String description, String status, Epic epic) {
        super(id, name, description, status);
        this.parentId = epic.id;
        //epic.addSubTask(this);
    }
    public int getParentId() {
        return parentId;
    }
    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description.lenght='" + description.length() + '\'' +
                ", status='" + status + '\'' +
                ", parentId=" + parentId +
                '}';
    }

}
