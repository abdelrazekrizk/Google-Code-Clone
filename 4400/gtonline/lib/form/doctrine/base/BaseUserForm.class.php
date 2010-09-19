<?php

/**
 * User form base class.
 *
 * @method User getObject() Returns the current form's model object
 *
 * @package    gtonline
 * @subpackage form
 * @author     Your name here
 * @version    SVN: $Id: sfDoctrineFormGeneratedTemplate.php 29553 2010-05-20 14:33:00Z Kris.Wallsmith $
 */
abstract class BaseUserForm extends BaseFormDoctrine
{
  public function setup()
  {
    $this->setWidgets(array(
      'id'          => new sfWidgetFormInputHidden(),
      'accountId'   => new sfWidgetFormDoctrineChoice(array('model' => $this->getRelatedModelName('Account'), 'add_empty' => false)),
      'sex'         => new sfWidgetFormInputText(),
      'birthday'    => new sfWidgetFormDate(),
      'currentcity' => new sfWidgetFormInputText(),
      'hometown'    => new sfWidgetFormInputText(),
    ));

    $this->setValidators(array(
      'id'          => new sfValidatorChoice(array('choices' => array($this->getObject()->get('id')), 'empty_value' => $this->getObject()->get('id'), 'required' => false)),
      'accountId'   => new sfValidatorDoctrineChoice(array('model' => $this->getRelatedModelName('Account'))),
      'sex'         => new sfValidatorInteger(),
      'birthday'    => new sfValidatorDate(),
      'currentcity' => new sfValidatorString(array('max_length' => 255, 'required' => false)),
      'hometown'    => new sfValidatorString(array('max_length' => 255, 'required' => false)),
    ));

    $this->widgetSchema->setNameFormat('user[%s]');

    $this->errorSchema = new sfValidatorErrorSchema($this->validatorSchema);

    $this->setupInheritance();

    parent::setup();
  }

  public function getModelName()
  {
    return 'User';
  }

}
