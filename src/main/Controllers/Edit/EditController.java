package main.Controllers.Edit;

import main.others.Utils.EditTypes;

/**
 * Created by User on 12.06.2018.
 */
public abstract class EditController<T>
{
    private int edit;
    private int result;
    private T currentObject;
    public void setParameters(int edit, T object)
    {
        this.edit=edit;
        currentObject=object;
    }

    protected void setResult(int result)
    {
        this.result=result;
    }
    public int getResult()
    {
        return result;
    }

    public T getCurrentObject()
    {
        return currentObject;
    }

    protected abstract void addObject();
    protected abstract void editObject();
    protected abstract void delObject();


    protected void doOperation()
    {
        switch (edit)
        {
            case EditTypes.ADDING: addObject();break;
            case EditTypes.EDITING: editObject();break;
            case EditTypes.DELETING: delObject();break;

        }
    }

}
